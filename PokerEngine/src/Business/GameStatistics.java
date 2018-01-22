package Business;

import Common.PlayerUtilities.PlayerInfo;

public class GameStatistics {
    private long timePastInSec;
    private int toalHandGames;
    private int totalPlayedHands;
    private int totalTokens;
    private PlayerInfo[] playerStatuses;

    public long getTimePastInSec() {
        return timePastInSec;
    }

    public void setTimePastInSec(long timePastInSec) {
        this.timePastInSec = timePastInSec;
    }

    public int getToalHandGames() {
        return toalHandGames;
    }

    public void setToalHandGames(int toalHandGames) {
        this.toalHandGames = toalHandGames;
    }

    public int getTotalPlayedHands() {
        return totalPlayedHands;
    }

    public void setTotalPlayedHands(int totalPlayedHands) {
        this.totalPlayedHands = totalPlayedHands;
    }

    public PlayerInfo[] getPlayerStatuses() {
        return playerStatuses;
    }

    public void setPlayerStatuses(PlayerInfo[] playerStatuses) {
        this.playerStatuses = playerStatuses;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }
}
