package Common.ExternalPlayer;

import Common.GlobalDefines.PokerAction;
import Common.PlayerUtilities.PlayerInfo;
import Common.PlayerUtilities.PokerTableView;

public interface IExternalPlayer {

    PokerAction GetNextAction(PlayerInfo playerInfo, PokerTableView tableView);
    void Fold(PlayerInfo playerInfo, PokerTableView tableView);
    int Bet(PlayerInfo playerInfo, PokerTableView tableView);
    void Call(PlayerInfo playerInfo, PokerTableView tableView);
    void Check(PlayerInfo playerInfo, PokerTableView tableView);
    int Raise(PlayerInfo playerInfo, PokerTableView tableView);
}
