package Common.Replay;

import Common.PlayerUtilities.PlayerInfo;

public class PlayerActionReplay {

    private PlayerInfo playerInfoSnapShot;
    private int newPot;
    private int newBetSize;

    public PlayerActionReplay(PlayerInfo playerInfoSnapShot, int newPot, int newBetSize) {
        this.playerInfoSnapShot = playerInfoSnapShot;
        this.newPot = newPot;
        this.newBetSize = newBetSize;
    }

    public PlayerInfo getPlayerInfoSnapShot() {
        return playerInfoSnapShot;
    }

    public int getNewPot() {
        return newPot;
    }

    public int getNewBetSize() {
        return newBetSize;
    }
}
