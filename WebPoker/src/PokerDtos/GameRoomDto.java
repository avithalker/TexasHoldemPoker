package PokerDtos;

public class GameRoomDto {

    private String gameTitle;
    private String owner;
    private int handsCount;
    private int buySize;
    private int smallBlind;
    private int bigBlind;
    private boolean isBlindFixed;
    private int blindAddition;
    private int registeredPlayers;
    private int maxPlayers;
    private String gameStatus;

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setHandsCount(int handsCount) {
        this.handsCount = handsCount;
    }

    public void setBuySize(int buySize) {
        this.buySize = buySize;
    }

    public void setSmallBlind(int smallBlind) {
        this.smallBlind = smallBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }

    public void setIsBlindFixed(boolean isBlindFixed) {
        this.isBlindFixed = isBlindFixed;
    }

    public void setBlindAddition(int blindAddition) {
        this.blindAddition = blindAddition;
    }

    public void setRegisteredPlayers(int registeredPlayers) {
        this.registeredPlayers = registeredPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }
}
