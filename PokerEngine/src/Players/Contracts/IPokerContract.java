package Players.Contracts;


import Common.ActionResult;
import Common.GlobalDefines.PokerAction;

public interface IPokerContract {

    void Fold();
    ActionResult Bet(int bet);
    ActionResult Call(int currBetSize);
    void Check();
    ActionResult Raise(int currBetSize,int raise);
}
