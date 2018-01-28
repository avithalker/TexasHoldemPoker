package ServletContexts;

import Business.GameManager;
import Business.GameSettings;
import Common.ActionResult;
import Common.GlobalDefines.RoomStatuses;
import PokerDtos.GameRoomDto;
import PokerDtos.PlayerSignInDto;

public class GameRoom {

    private UsersManager roomUserManager;
    private GameManager gameManager;
    private RoomStatuses gameStatus;
    private String roomOwner;

    public GameRoom(String roomOwner){
        this.roomOwner = roomOwner;
        gameStatus = RoomStatuses.Pending;
        roomUserManager = new UsersManager();
        gameManager = new GameManager();
    }

    public boolean joinRoom(PlayerSignInDto playerDetails){
        return roomUserManager.addUser(playerDetails);
    }

    public void leaveRoom(String playerName){
        roomUserManager.removeUser(playerName);
    }

    public synchronized ActionResult loadRoomSettings(String settings){
        ActionResult result = gameManager.loadGameSettingsFromString(settings);

        return result;
    }

    public GameRoomDto getRoomDetails(){

        GameRoomDto roomDetails = new GameRoomDto();
        GameSettings settings = gameManager.getGameSettings();
        roomDetails.setOwner(roomOwner);
        roomDetails.setBigBlind(settings.getBigBlindValue());
        roomDetails.setBlindAddition(settings.getBlindAddition());
        roomDetails.setBuySize(settings.getBuyValue());
        roomDetails.setHandsCount(settings.getHandsCount());
        roomDetails.setSmallBlind(settings.getSmallBlindValue());
        roomDetails.setIsBlindFixed(settings.isBlindFixed());
        roomDetails.setGameStatus(gameStatus.toString());
        roomDetails.setRegisteredPlayers(roomUserManager.getUsersCount());
        roomDetails.setGameTitle(settings.getGameTitle());
        roomDetails.setMaxPlayers(settings.getTotalPlayers());

        return  roomDetails;
    }



}
