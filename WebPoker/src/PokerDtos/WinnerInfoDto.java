package PokerDtos;

import Common.GlobalDefines.PlayerTypes;

public class WinnerInfoDto {

    private String playerName;
    private PlayerTypes playerType;
    private int totalBuys;
    private int winningPrice;
    private int totalWinsNumber;
    private int totalHandsPlayed;
    private String handRank;

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setPlayerType(PlayerTypes playerType) {
        this.playerType = playerType;
    }

    public void setTotalBuys(int totalBuys) {
        this.totalBuys = totalBuys;
    }

    public void setWinningPrice(int winningPrice) {
        this.winningPrice = winningPrice;
    }

    public void setTotalWinsNumber(int totalWinsNumber) {
        this.totalWinsNumber = totalWinsNumber;
    }

    public void setTotalHandsPlayed(int totalHandsPlayed) {
        this.totalHandsPlayed = totalHandsPlayed;
    }

    public void setHandRank(String handRank) {
        this.handRank = handRank;
    }
}
