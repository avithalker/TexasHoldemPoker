package Business;

import Common.GlobalDefines.BetRoundTitles;
import Common.GlobalDefines.PokerAction;
import Common.GlobalDefines.PokerTitle;
import Players.Player;

import java.lang.reflect.Array;
import java.util.Arrays;

public class GambleRoundManager {

    private boolean isFirstBetReceived;
    private int currPlayerPlace;
    private int gambleRoundNo;
    private BetRoundTitles [] roundStates;
    private Player lastRaisedPlayer;
    private Player firstPlayerToBet;
    private boolean isHandRoundStarted;
    private boolean isHandRoundEnded;


    public GambleRoundManager(){

        gambleRoundNo = 0;
        roundStates = new BetRoundTitles[]{BetRoundTitles.FirstBetRound,
                BetRoundTitles.Flop,BetRoundTitles.Turn,BetRoundTitles.River};
        isHandRoundStarted = false;
        isHandRoundEnded = false;
    }


    public boolean isFirstBetReceived() {

        return isFirstBetReceived;
    }


    public void setFirstBetReceived(boolean firstBetReceived, Player firstPlayerToBet) {

        isFirstBetReceived = firstBetReceived;
        if(firstBetReceived)
            this.firstPlayerToBet = firstPlayerToBet;
    }

    public int getCurrPlayerPlace() {
        return currPlayerPlace;
    }

    public void setLastRaisedPlayer(Player lastRaisedPlayer) {
        this.lastRaisedPlayer = lastRaisedPlayer;
    }

    public boolean isHandDone(Player[] playersInRound, int betValue){
        return (roundStates[gambleRoundNo - 1] == BetRoundTitles.River &&
                isGambleRoundDone(playersInRound, betValue)) || isLastManStanding(playersInRound) || isHandRoundEnded;
    }

    private boolean isLastManStanding(Player[] playersInRound){
        int foldedPlayers = 0;
        for(Player player : playersInRound){
            if(player.getLastAction() == PokerAction.FOLD)
                foldedPlayers++;
        }
        if(foldedPlayers == playersInRound.length -1)
            return true;
        return false;
    }

    public boolean isGambleRoundDone(Player[] playersInRound, int betValue){
        boolean result = Arrays.stream(playersInRound).allMatch(s-> s.getLastAction() == PokerAction.CALL ||
                s.getLastAction() == PokerAction.FOLD ||
                (s.getLastAction() == PokerAction.CHECK && s.getTitle() == PokerTitle.BIG));
        if(result)
            return true;

        result = Arrays.stream(playersInRound).allMatch(s-> s.getLastAction() == PokerAction.CHECK ||
                s.getLastAction() == PokerAction.FOLD);
        if(result)
            return true;

        result = Arrays.stream(playersInRound).allMatch(s-> ((s.getLastAction() == PokerAction.CALL ||
                        (s.getLastAction() == PokerAction.RAISE && s.getPlayerId() == lastRaisedPlayer.getPlayerId()))
                        && s.getCurrentBet() == betValue)||
                        s.getLastAction() == PokerAction.FOLD);
        if(result) {
            return true;
        }

        result = Arrays.stream(playersInRound).allMatch(s-> s.getLastAction() == PokerAction.CALL ||
                s.getLastAction() == PokerAction.FOLD ||
                (s.getLastAction() == PokerAction.BET && s.getPlayerId() == firstPlayerToBet.getPlayerId()));
        if(result)
            return true;

        result = Arrays.stream(playersInRound).allMatch(s-> s.getLastAction() == PokerAction.CALL ||
                s.getLastAction() == PokerAction.FOLD ||
                (s.getLastAction() == PokerAction.BET && s.getPlayerId() == firstPlayerToBet.getPlayerId())||
                (s.getLastAction() == PokerAction.CHECK &&s.getTitle() == PokerTitle.BIG && s.getCurrentBet() == betValue));
        if(result)
            return true;

        result = isLastManStanding(playersInRound);
        if(result)
            return true;

        return false;
    }

    public void startNewHand(){
        this.gambleRoundNo = 0;
        this.isFirstBetReceived = false;
        this.lastRaisedPlayer = null;
        this.firstPlayerToBet = null;
        isHandRoundStarted = true;
        isHandRoundEnded = false;
    }

    public void endHand(){
        isHandRoundStarted = false;
        isHandRoundEnded = true;
    }

    public BetRoundTitles startNewGambleRound(Player[] playersInRound, int dealerPlace){

        if(gambleRoundNo >= roundStates.length)
            return BetRoundTitles.None;
        isFirstBetReceived = false;
        lastRaisedPlayer = null;
        firstPlayerToBet = null;

        if(roundStates[gambleRoundNo] == BetRoundTitles.FirstBetRound)
            this.currPlayerPlace = (dealerPlace + 3) % playersInRound.length;
        else {
            this.currPlayerPlace = (dealerPlace + 1) % playersInRound.length;
            if (playersInRound[currPlayerPlace].getLastAction() == PokerAction.FOLD) {
                boolean found = false;
                do {
                    this.currPlayerPlace = (currPlayerPlace + 1) % playersInRound.length;
                    if (playersInRound[this.currPlayerPlace].getLastAction() != PokerAction.FOLD)
                        found = true;
                } while (!found);
            }
        }

        BetRoundTitles betRoundTitle = roundStates[gambleRoundNo];
        gambleRoundNo++;
        return betRoundTitle;
    }

    public void moveToNextPlayer(Player [] playersInRound){
        boolean found = false;
        do {
            this.currPlayerPlace = (currPlayerPlace + 1) % playersInRound.length;

            if(playersInRound[this.currPlayerPlace].getLastAction() != PokerAction.FOLD)
                found = true;
        }while (!found);
    }

    public boolean isHandRoundStarted() {
        return isHandRoundStarted;
    }
}
