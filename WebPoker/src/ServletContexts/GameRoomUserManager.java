package ServletContexts;

import PokerDtos.PlayerSignInDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameRoomUserManager extends UsersManager {
    private Map<String,Boolean> playersReadyStatus;

    public GameRoomUserManager(){
        playersReadyStatus = new HashMap<>();
    }

    @Override
    public synchronized boolean addUser(PlayerSignInDto playerSignInDto) {
        boolean result = super.addUser(playerSignInDto);
        if(result)
            playersReadyStatus.put(playerSignInDto.getPlayerName(),false);
        return result;
    }

    @Override
    public synchronized void removeUser(String playerName) {
        super.removeUser(playerName);
        playersReadyStatus.remove(playerName);
    }

    public synchronized void setReadyStatus(String playerName, boolean isReady){
        playersReadyStatus.put(playerName,isReady);
    }

    public synchronized boolean isAllReady(){
        for (boolean isPlayerReady:playersReadyStatus.values()){
            if(isPlayerReady == false)
                return false;
        }
        return true;
    }

    public synchronized void InitAllReadyStatuses(){
         Set<String> keys = playersReadyStatus.keySet();
         for(String key:keys){
             playersReadyStatus.put(key,false);
         }
    }
}
