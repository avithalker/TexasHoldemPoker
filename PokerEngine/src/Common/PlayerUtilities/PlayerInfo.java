package Common.PlayerUtilities;

import Common.GlobalDefines.PlayerTypes;
import Common.GlobalDefines.PokerAction;
import Common.GlobalDefines.PokerTitle;
import javafx.beans.property.SimpleStringProperty;

public class PlayerInfo {

    private int playerId;
    private String playerName;
    private PokerTitle playerTitle;
    private PlayerTypes playerType;
    private int tokens;
    private int currentBet;
    private int totalBuys;
    private int totalWins;
    private int totalHandsPlayed;
    private String[] playerCards;
    private PokerAction lastAction;
    private String totalWinsStatistics;
    private boolean isPlayerFoldFromEntireGame;

    public PlayerInfo() {
    }


    public int getPlayerId(){
        return this.playerId;
    }

    public int getTokens(){
        return  this.tokens;
    }

    public int getCurrentBet(){
        return this.currentBet;
    }

    public PlayerTypes getPlayerType() {
        return playerType;
    }

    public PokerTitle getPlayerTitle() {
        return playerTitle;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
    }

    public void setPlayerType(PlayerTypes playerType) {
        this.playerType = playerType;
    }

    public void setPlayerTitle(PokerTitle playerTitle) {
        this.playerTitle = playerTitle;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public String[] getPlayerCards() {
        return playerCards;
    }

    public void setPlayerCards(String[] playerCards) {
        this.playerCards = playerCards;
    }

    public int getTotalBuys() {
        return totalBuys;
    }

    public void setTotalBuys(int totalBuys) {
        this.totalBuys = totalBuys;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getTotalHandsPlayed() {
        return totalHandsPlayed;
    }

    public void setTotalHandsPlayed(int totalHandsPlayed) {
        this.totalHandsPlayed = totalHandsPlayed;
    }

    public PokerAction getLastAction() {
        if(lastAction != null)
            return lastAction;
        return PokerAction.NONE;
    }

    public void setLastAction(PokerAction lastAction) {
        this.lastAction = lastAction;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getTotalWinsStatistics() {
        return totalWins + "/" + totalHandsPlayed;
    }

    public boolean isPlayerFoldFromEntireGame() {
        return isPlayerFoldFromEntireGame;
    }

    public void setPlayerFoldFromEntireGame(boolean playerFoldFromEntireGame) {
        isPlayerFoldFromEntireGame = playerFoldFromEntireGame;
    }
}
