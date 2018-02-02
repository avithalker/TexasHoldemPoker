package ServletContexts;

import PokerDtos.PlayerSignInDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersManager {

    private Map<String, PlayerSignInDto> onlineUsers;

    public UsersManager(){
        onlineUsers = new HashMap<>();
    }

    public synchronized boolean addUser(PlayerSignInDto playerSignInDto){
        if(isExist(playerSignInDto.getPlayerName()))
            return false;
        onlineUsers.put(playerSignInDto.getPlayerName(),playerSignInDto);
        return true;
    }

    public synchronized void removeUser(String playerName){
        if(!isExist(playerName))
            return;
        onlineUsers.remove(playerName);
    }

    public synchronized boolean isExist(String playerName){
        return onlineUsers.containsKey(playerName);
    }

    public synchronized List<String> getAllUsers(){
        List<String> userList=new ArrayList<>();
        for(String name:onlineUsers.keySet())
            userList.add(name);
        return userList;
    }

    public synchronized PlayerSignInDto getUser(String playerName){
        return onlineUsers.getOrDefault(playerName,null);
    }

    public synchronized int getUsersCount(){
        return onlineUsers.size();
    }
}
