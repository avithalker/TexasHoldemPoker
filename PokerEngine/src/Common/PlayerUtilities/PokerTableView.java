package Common.PlayerUtilities;

import Common.GlobalDefines.PokerAction;

public class PokerTableView {

    private String[] tableCards;
    private int pot;
    private boolean isFirstBetReceived;
    private int betSize;
    private int maxBetValue;
    private int bigValue;
    private int smallValue;

    public String[] getTableCards() {
        return tableCards;
    }

    public void setTableCards(String[] tableCards) {
        this.tableCards = tableCards;
    }

    public int getPot() {
        return pot;
    }

    public void setPot(int pot) {
        this.pot = pot;
    }

    public boolean isFirstBetReceived() {
        return isFirstBetReceived;
    }

    public void setFirstBetReceived(boolean firstBetReceived) {
        isFirstBetReceived = firstBetReceived;
    }

    public int getBetSize() {
        return betSize;
    }

    public void setBetSize(int betSize) {
        this.betSize = betSize;
    }

    public int getBigValue() {
        return bigValue;
    }

    public void setBigValue(int bigValue) {
        this.bigValue = bigValue;
    }

    public int getSmallValue() {
        return smallValue;
    }

    public void setSmallValue(int smallValue) {
        this.smallValue = smallValue;
    }

    public int getMaxBetValue() {
        return maxBetValue;
    }

    public void setMaxBetValue(int maxBetValue) {
        this.maxBetValue = maxBetValue;
    }
}
