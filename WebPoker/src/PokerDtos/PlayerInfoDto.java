package PokerDtos;



public class PlayerInfoDto {

    private String playerName;
    private String playerType;
    private int tokens;
    private int totalBuys;
    private int totalWins;
    private int totalHandsPlayed;

    public void setTokens(int tokens) {
        this.tokens = tokens;
    }

    public void setTotalBuys(int totalBuys) {
        this.totalBuys = totalBuys;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public void setTotalHandsPlayed(int totalHandsPlayed) {
        this.totalHandsPlayed = totalHandsPlayed;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
