package Common.PlayerUtilities;

import Common.GlobalDefines.PlayerTypes;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.awt.font.TextHitInfo;

public class WinnerInfo {

    private int playerId;
    private String name;
    private PlayerTypes playerType;
    private int totalBuys;
    private String totalWins;
    private int winningPrice;
    private int totalWinsNumber;
    private int totalHandsPlayed;
    private String handRank;

    public WinnerInfo(int playerId, String handRank){
        this.playerId = playerId;
        this.handRank = handRank;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getWinningPrice() {
        return winningPrice;
    }

    public void setWinningPrice(int winningPrice) {
        this.winningPrice = winningPrice;
    }

    public String getHandRank() {
        return handRank;
    }

    public void setHandRank(String handRank) {
        this.handRank = handRank;
    }

    @Override
    public String toString() {
        return "Player Id: " + playerId + "\n" +
                "Hand rank: " + handRank + "\n" +
                "Winning price: " + winningPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerTypes getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerTypes playerType) {
        this.playerType = playerType;
    }

    public int getTotalBuys() {
        return totalBuys;
    }

    public void setTotalBuys(int totalBuys) {
        this.totalBuys = totalBuys;
    }

    public String getTotalWins() {
        return totalWinsNumber+"/"+totalHandsPlayed;
    }

    public int getTotalWinsNumber() {
        return totalWinsNumber;
    }

    public void setTotalWinsNumber(int totalWinsNumber) {
        this.totalWinsNumber = totalWinsNumber;
    }

    public int getTotalHandsPlayed() {
        return totalHandsPlayed;
    }

    public void setTotalHandsPlayed(int totalHandsPlayed) {
        this.totalHandsPlayed = totalHandsPlayed;
    }
}
