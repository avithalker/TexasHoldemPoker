package Common.ExternalPlayer;

import Common.GlobalDefines.PokerAction;
import Common.PlayerUtilities.PlayerInfo;
import Common.PlayerUtilities.PokerTableView;

public class ComputerPlayer implements IExternalPlayer {
    @Override
    public PokerAction GetNextAction(PlayerInfo playerInfo, PokerTableView tableView) {

        if(tableView.isFirstBetReceived()){
            if(playerInfo.getCurrentBet() == tableView.getBetSize())
                return PokerAction.CHECK;
            return PokerAction.CALL;
        }
        else {
            if (tableView.getBigValue() + 10 > playerInfo.getTokens())
                return PokerAction.FOLD;
            else
                return PokerAction.BET;
        }
    }

    @Override
    public void Fold(PlayerInfo playerInfo, PokerTableView tableView) {

    }

    @Override
    public int Bet(PlayerInfo playerInfo, PokerTableView tableView) {

        if (tableView.getBigValue() + 10 < tableView.getMaxBetValue()) {
            if (tableView.getBigValue() + 10 > tableView.getPot())
                return tableView.getPot();
            return tableView.getBigValue() + 10;
        }
        return tableView.getMaxBetValue();
    }

    @Override
    public void Call(PlayerInfo playerInfo, PokerTableView tableView) {

    }

    @Override
    public void Check(PlayerInfo playerInfo, PokerTableView tableView) {

    }

    @Override
    public int Raise(PlayerInfo playerInfo, PokerTableView tableView) {
        return 0;
    }
}
