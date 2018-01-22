package Common.Replay;

import Common.GlobalDefines.BetRoundTitles;
import Common.PlayerUtilities.PlayerInfo;
import Common.PlayerUtilities.PokerTableView;

import java.util.ArrayList;

public class HandReplay {

    private RoundReplay currRoundReplay;
    private ArrayList<RoundReplay> roundReplays;
    private PlayerInfo[] activePlayers;
    private PlayerInfo[] allPlayers;
    private PokerTableView tableInfo;

    public HandReplay(){

        roundReplays = new ArrayList<>();
    }

    public ArrayList<RoundReplay> getRoundReplays() {
        return roundReplays;
    }

    public void addNewRoundReplay(BetRoundTitles betRoundTitle, PokerTableView tableInfo, PlayerInfo[] playersInfo){
        currRoundReplay = new RoundReplay(betRoundTitle,tableInfo, playersInfo);
        roundReplays.add(currRoundReplay);
    }

    public void addNewPlayerAction(PlayerInfo playerInfoSnapShot, int newPot, int newBetSize){
        currRoundReplay.addNewRoundAction(playerInfoSnapShot, newPot, newBetSize);
    }

    public void clearReplay(){
        currRoundReplay = null;
        roundReplays.clear();
        activePlayers = null;
        tableInfo = null;
    }

    public PlayerInfo[] getActivePlayers() {
        return activePlayers;
    }

    public void setActivePlayers(PlayerInfo[] players) {
        this.activePlayers = players;
    }

    public PokerTableView getTableInfo() {
        return tableInfo;
    }
    public void setTableInfo(PokerTableView tableInfo) {
         this.tableInfo = tableInfo;
    }

    public PlayerInfo[] getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(PlayerInfo[] allPlayers) {
        this.allPlayers = allPlayers;
    }
}
