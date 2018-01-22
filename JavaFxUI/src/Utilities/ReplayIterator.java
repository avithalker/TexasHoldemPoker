package Utilities;

import Common.PlayerUtilities.PlayerInfo;
import Common.PlayerUtilities.PokerTableView;
import Common.Replay.HandReplay;
import Players.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class ReplayIterator {
    private HandReplay handReplay;
    int currRoundIndex;
    int currActionIndex;
    int prevPlayerId;
    private HashMap<Integer,ArrayList<PlayerInfo>> playersPrevActions;

    public ReplayIterator(HandReplay handReplay){
        playersPrevActions=new HashMap<>();
        this.handReplay = handReplay;
        currActionIndex = -1;
        currRoundIndex = - 1;
        prevPlayerId = -1;
    }

    public boolean isNewRound(){
        if(currActionIndex == 0)
            return true;
        return false;
    }



    public int getCuurPot(){
        if(currRoundIndex == -1 && currActionIndex == -1)
            return 0;
        if(handReplay.getRoundReplays().get(currRoundIndex).getRoundActions().size() == 0)
            return handReplay.getRoundReplays().get(currRoundIndex).getTableInfo().getPot();

        return handReplay.getRoundReplays().get(currRoundIndex).getRoundActions().get(currActionIndex).getNewPot();
    }

    public  PlayerInfo[] getActivePlayers(){
        if(currRoundIndex == -1)
            return handReplay.getActivePlayers();
        return handReplay.getRoundReplays().get(currRoundIndex).getPlayersInfo();
    }

    public PlayerInfo[] getAllPlayersLastAction(){
        PlayerInfo[] playersLastActions= new PlayerInfo[playersPrevActions.size()];;
        int i = 0;
        for(int playerId:playersPrevActions.keySet()){
            ArrayList<PlayerInfo> playerPrevActions = playersPrevActions.get(playerId);
            playersLastActions[i] = playerPrevActions.get(playerPrevActions.size() - 1);
            i++;
        }
        return  playersLastActions;
    }

    public ArrayList<PlayerInfo> getDisablePlayers(){
        ArrayList<PlayerInfo> disablePlayers = new ArrayList<>();
        PlayerInfo[] allPlayers = handReplay.getAllPlayers();
        PlayerInfo[] activePlayers = handReplay.getActivePlayers();
        for(PlayerInfo player:allPlayers){
            boolean isActive = false;
            for(PlayerInfo activePlayer: activePlayers){
                if(player.getPlayerId() == activePlayer.getPlayerId()) {
                    isActive = true;
                    break;
                }
            }
            if(!isActive)
                disablePlayers.add(player);
        }
        return disablePlayers;
    }

    public PokerTableView getCurrTableView(){
        if(currRoundIndex == -1){
            return handReplay.getTableInfo();
        }
        return handReplay.getRoundReplays().get(currRoundIndex).getTableInfo();
    }

    public PlayerInfo getCurrPlayerActionResult(){
        if(currActionIndex == -1 || handReplay.getRoundReplays().get(currRoundIndex).getRoundActions().size() == 0)
            return null;
        return handReplay.getRoundReplays().get(currRoundIndex).getRoundActions().get(currActionIndex).getPlayerInfoSnapShot();
    }

    public PlayerInfo getLastPlayerAction(){
        if(!playersPrevActions.containsKey(prevPlayerId))
            return null;
        ArrayList<PlayerInfo> playerPrevActions = playersPrevActions.get(prevPlayerId);
        if(playerPrevActions.size()==0)
            return null;
        return playerPrevActions.get(playerPrevActions.size() - 1);
    }

    private void addForAllPrevAction(){
        PlayerInfo[] actions= getActivePlayers();
        for(int i=0;i<actions.length;i++){
            if(!playersPrevActions.containsKey(actions[i].getPlayerId()))
                playersPrevActions.put(actions[i].getPlayerId(), new ArrayList<>());
            playersPrevActions.get(actions[i].getPlayerId()).add(actions[i]);
        }
    }

    private void removeForAllPrevAction(){
        PlayerInfo[] actions= getActivePlayers();
        for(int i=0;i<actions.length;i++){
            ArrayList<PlayerInfo>playerActions = playersPrevActions.get(actions[i].getPlayerId());
            if(playerActions.size() - 1 > 0)
                playerActions.remove(playerActions.size()-1);
        }
    }

    public boolean forward(){
        boolean result = false;
        if(currRoundIndex == -1 && currActionIndex == -1)
        {
            currRoundIndex++;
            currActionIndex++;
            addForAllPrevAction();
            playersPrevActions.get(getCurrPlayerActionResult().getPlayerId()).add(getCurrPlayerActionResult());
            result = true;
        }
        else if(currActionIndex + 1 < handReplay.getRoundReplays().get(currRoundIndex).getRoundActions().size())
        {
            currActionIndex++;
            playersPrevActions.get(getCurrPlayerActionResult().getPlayerId()).add(getCurrPlayerActionResult());
            result = true;
        }
        else
        {
            if(currRoundIndex + 1 < handReplay.getRoundReplays().size()){
                currActionIndex = 0;
                currRoundIndex++;
                addForAllPrevAction();
                result = true;
            }
        }
        if(result){
           // if(!playersPrevActions.containsKey(getCurrPlayerActionResult().getPlayerId()))
             //   playersPrevActions.put(getCurrPlayerActionResult().getPlayerId(), new ArrayList<>());
            //playersPrevActions.get(getCurrPlayerActionResult().getPlayerId()).add(getCurrPlayerActionResult());
            PlayerInfo currPlayerActionResult = getCurrPlayerActionResult();
            if(currPlayerActionResult!=null)
                prevPlayerId = getCurrPlayerActionResult().getPlayerId();
        }

        return result;
    }
    public boolean backward(){
        prevPlayerId = -1;
        if((currRoundIndex == -1 && currActionIndex == -1) || (currRoundIndex == 0 && currActionIndex == 0)){
            return false;
        }

        if(currActionIndex - 1 >=0 ){

            ArrayList<PlayerInfo>playerActions = playersPrevActions.get(getCurrPlayerActionResult().getPlayerId());
            playerActions.remove(playerActions.size()-1);
            prevPlayerId = getCurrPlayerActionResult().getPlayerId();
            currActionIndex --;
            return true;
        }
        else{
            if(currRoundIndex - 1 >= 0){

                //ArrayList<PlayerInfo>playerActions = playersPrevActions.get(getCurrPlayerActionResult().getPlayerId());
                //playerActions.remove(playerActions.size()-1);
                removeForAllPrevAction();
                if(getCurrPlayerActionResult() != null)
                    prevPlayerId = getCurrPlayerActionResult().getPlayerId();
                currRoundIndex--;
                currActionIndex = handReplay.getRoundReplays().get(currRoundIndex).getRoundActions().size() - 1;
                return true;
            }
        }
        return false;
    }
}
