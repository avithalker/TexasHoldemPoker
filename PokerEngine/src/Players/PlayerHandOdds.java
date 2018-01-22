package Players;

public class PlayerHandOdds implements Comparable<PlayerHandOdds> {

    private int winningOdss;
    private int playerId;
    private String handRank;

    public PlayerHandOdds(int playerId, int winningOdss, String handRank){
        this.playerId = playerId;
        this.winningOdss = winningOdss;
        this.handRank = handRank;
    }

    public int getWinningOdss() {
        return winningOdss;
    }

    public void setWinningOdss(int winningOdss) {
        this.winningOdss = winningOdss;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public int compareTo(PlayerHandOdds o) {
        if(winningOdss == o.winningOdss)
            return 0;
        else if(winningOdss > o.winningOdss)
            return 1;
        else
            return -1;
    }

    public String getHandRank() {
        return handRank;
    }
}
