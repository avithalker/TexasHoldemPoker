package Common.PlayerUtilities;

import Common.GlobalDefines.PlayerTypes;

public class PlayerRegistration {

    private int playerId;
    private String playerName;
    private PlayerTypes playerType;

    public PlayerRegistration(int playerId, PlayerTypes playerType,String playerName){
        this.playerId = playerId;
        this.playerName = playerName;
        this.playerType = playerType;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public PlayerTypes getPlayerType() {
        return playerType;
    }

    public void setPlayerType(PlayerTypes playerType) {
        this.playerType = playerType;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
