package ServletContexts;

import Common.ActionResult;
import PokerDtos.GameRoomDto;
import PokerDtos.PlayerSignInDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameRoomManager {
    private Map<String,GameRoom> rooms;
    private Map<String,GameRoom> playersRoom;

    public GameRoomManager(){
        rooms = new HashMap<>();
        playersRoom = new HashMap<>();
    }

    public synchronized ActionResult addNewRoom(String roomSettings, String roomOwner){
        GameRoom newRoom = new GameRoom(roomOwner);
        ActionResult result = newRoom.loadRoomSettings(roomSettings);
        if(!result.isSucceed())
            return result;

        GameRoomDto roomDetails = newRoom.getRoomDetails();
        if(isRoomExist(roomDetails.getGameTitle()))
            return new ActionResult(false,"Failed to create room- room with this title is already exist");

        rooms.put(roomDetails.getGameTitle(),newRoom);
        return result;
    }

    public synchronized void removeRoom(String roomName){
        if(isRoomExist(roomName))
            rooms.remove(roomName);
    }

    public synchronized ActionResult joinRoom(String roomName, PlayerSignInDto player){
        if(playersRoom.containsKey(player.getPlayerName())){
            return new ActionResult(false,"Can't join the room because The player is already in different room");
        }
        if(!isRoomExist(roomName)){
            return new ActionResult(false,"Can't join the room because the room doesn't exist");
        }
        GameRoom room = getRoomByName(roomName);
        boolean result = room.joinRoom(player);
        if(!result)
            return new ActionResult(false,"Can't join the room- the room is full");
        playersRoom.put(player.getPlayerName(),room);
        return new ActionResult(true,"");
    }

    public synchronized void leaveRoom(String playerName){
        GameRoom room = playersRoom.getOrDefault(playerName,null);
        if(room == null)
            return;
        room.leaveRoom(playerName);
        playersRoom.remove(playerName);
    }

    public synchronized GameRoom getRoomByName(String roomName){
        return rooms.getOrDefault(roomName,null);
    }

    public synchronized GameRoom getRoomByPlayer(String playerName){
        return playersRoom.getOrDefault(playerName,null);
    }

    public synchronized List<GameRoomDto> getAllRoomsDetails(){
        List<GameRoomDto> roomsDetails = new ArrayList<>();
        for(String roomName:rooms.keySet()){
            roomsDetails.add(rooms.get(roomName).getRoomDetails());
        }
        return roomsDetails;
    }

    public synchronized boolean isRoomExist(String roomName){
        return rooms.containsKey(roomName);
    }

}
