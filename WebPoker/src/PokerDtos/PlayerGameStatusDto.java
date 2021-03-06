package PokerDtos;

public class PlayerGameStatusDto {
    private String playerName;
    private String playerTitle;
    private int tokens;
    private int currentBet;
    private String[] playerCards;
    private String lastAction;
    private boolean isActive;
    private boolean isMyTurn;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerTitle(String playerTitle) {
        this.playerTitle = playerTitle;
    }

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public void setCurrentBet(int currentBet) {
        this.currentBet = currentBet;
    }

    public void setPlayerCards(String[] playerCards) {
        this.playerCards = playerCards;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setMyTurn(boolean myTurn) {
        isMyTurn = myTurn;
    }

}

