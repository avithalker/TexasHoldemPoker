package Common.Replay;

import Common.GlobalDefines.BetRoundTitles;
import Common.PlayerUtilities.PlayerInfo;
import Common.PlayerUtilities.PokerTableView;

import java.util.ArrayList;

public class RoundReplay {
    private BetRoundTitles betRoundTitle;
    private PokerTableView tableInfo;
    private ArrayList<PlayerActionReplay> roundActions;
    private PlayerInfo[] playersInfo;


    public  RoundReplay(BetRoundTitles betRoundTitle, PokerTableView tableInfo, PlayerInfo[] playersInfo){
        roundActions = new ArrayList<>();
        this.betRoundTitle = betRoundTitle;
        this.tableInfo = tableInfo;
        this.playersInfo = playersInfo;
    }

    public BetRoundTitles getBetRoundTitle() {
        return betRoundTitle;
    }

    public ArrayList<PlayerActionReplay> getRoundActions() {
        return roundActions;
    }

    public void addNewRoundAction(PlayerInfo playerInfoSnapShot,int newPot, int newBetSize){

        roundActions.add(new PlayerActionReplay(playerInfoSnapShot, newPot, newBetSize));
    }

    public PokerTableView getTableInfo() {
        return tableInfo;
    }

    public PlayerInfo[] getPlayersInfo() {
        return playersInfo;
    }
}
