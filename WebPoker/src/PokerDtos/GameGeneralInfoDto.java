package PokerDtos;

public class GameGeneralInfoDto {
    private int buyValue;
    private int handsCount;
    private int bigBlindValue;
    private int smallBlindValue;
    private boolean isBlindFixed;
    private int blindAddition;
    private int totalTokens;
    private int totalPlayedHands;

    public int getBuyValue() {
        return buyValue;
    }

    public void setBuyValue(int buyValue) {
        this.buyValue = buyValue;
    }

    public int getHandsCount() {
        return handsCount;
    }

    public void setHandsCount(int handsCount) {
        this.handsCount = handsCount;
    }

    public int getBigBlindValue() {
        return bigBlindValue;
    }

    public void setBigBlindValue(int bigBlindValue) {
        this.bigBlindValue = bigBlindValue;
    }

    public int getSmallBlindValue() {
        return smallBlindValue;
    }

    public void setSmallBlindValue(int smallBlindValue) {
        this.smallBlindValue = smallBlindValue;
    }

    public boolean isBlindFixed() {
        return isBlindFixed;
    }

    public void setBlindFixed(boolean blindFixed) {
        isBlindFixed = blindFixed;
    }

    public int getBlindAddition() {
        return blindAddition;
    }

    public void setBlindAddition(int blindAddition) {
        this.blindAddition = blindAddition;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
    }

    public int getTotalPlayedHands() {
        return totalPlayedHands;
    }

    public void setTotalPlayedHands(int totalPlayedHands) {
        this.totalPlayedHands = totalPlayedHands;
    }
}
