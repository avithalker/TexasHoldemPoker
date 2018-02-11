package ServletContexts;

import PokerDtos.PlayerSignInDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersManager {

    private Map<String, PlayerSignInDto> onlineUsers;
    private List<String> signInOrder;

    public UsersManager(){

        onlineUsers = new HashMap<>();
        signInOrder = new ArrayList<>();
    }

    public synchronized boolean addUser(PlayerSignInDto playerSignInDto){
        if(isExist(playerSignInDto.getPlayerName()))
            return false;
        onlineUsers.put(playerSignInDto.getPlayerName(),playerSignInDto);
        signInOrder.add(playerSignInDto.getPlayerName());
        return true;
    }

    public synchronized void removeUser(String playerName){
        if(!isExist(playerName))
            return;
        onlineUsers.remove(playerName);
        signInOrder.remove(playerName);
    }

    public synchronized boolean isExist(String playerName){

        return onlineUsers.containsKey(playerName);
    }

    public synchronized List<String> getAllUsersNames(){
        List<String> userList=new ArrayList<>();
        for(String name:onlineUsers.keySet())
            userList.add(name);
        return userList;
    }

    public synchronized PlayerSignInDto [] getAllUsers(){
        return  (PlayerSignInDto [])onlineUsers.values().toArray();
    }

    public synchronized PlayerSignInDto [] getAllUsersOrdered(){
        PlayerSignInDto [] users = new PlayerSignInDto[signInOrder.size()];
        for(int i=0;i < signInOrder.size();i++){
            users[i] = onlineUsers.get(signInOrder.get(i));
        }
        return users;
    }

    public synchronized PlayerSignInDto getUser(String playerName){

        return onlineUsers.getOrDefault(playerName,null);
    }

    public synchronized int getUsersCount(){
        return onlineUsers.size();
    }
}
