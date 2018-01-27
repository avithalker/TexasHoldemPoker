package Business;

import Common.ActionResult;
import Common.GlobalDefines.GameTypes;
import Common.GlobalDefines.PlayerTypes;
import Common.PlayerUtilities.PlayerRegistration;
import generatedJaxb.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class GameSettings {

    private String gameType;
    private int buyValue;
    private int handsCount;
    private int bigBlindValue;
    private int bigBlindMaxValue;
    private int smallBlindValue;
    private boolean isBlindFixed;
    private int blindAddition;
    private int blindMaxTotalRound;
    private int numberOfPlayers;
    private int totalPlayers;
    private String gameTitle;
    private List<Player> definedPlayers;
    private ArrayList<PlayerRegistration> fixedPlayersRegistration;

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

    public int getBlindMaxTotalRound() {
        return blindMaxTotalRound;
    }

    public void setBlindMaxTotalRound(int blindMaxTotalRound) {
        this.blindMaxTotalRound = blindMaxTotalRound;
    }

    public GameTypes getGameType() {
        return GameTypes.valueOf(gameType);
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public void setDefinedPlayers(List<Player> definedPlayers) {
        this.definedPlayers = definedPlayers;
    }

    public int getBigBlindMaxValue() {
        return bigBlindMaxValue;
    }

    public ArrayList<PlayerRegistration> getFixedPlayersRegistration() {
        return fixedPlayersRegistration;
    }

    public ActionResult isGeneralSettingsValid()
    {
        ActionResult result = new ActionResult(true,"");

        if(buyValue <= 0){
            result.setSucceed(false);
            result.setMsgError("!!! Error in settings file. buy must have positive value !!!");
            return result;
        }

        if(handsCount <=0) {
            result.setSucceed(false);
            result.setMsgError("!!! Error in settings file. handsCount must have positive value !!!");
            return result;
        }

        if(getGameType() == GameTypes.MultiPlayer){
            result = loadPredefinedPlayers();
            if(!result.isSucceed())
                return result;
        }

        result = isBlindsValid();
        if(!result.isSucceed())
            return result;

        return result;
    }

    private ActionResult isBlindsValid(){
        ActionResult result = new ActionResult(true,"");

        if(bigBlindValue < 0){
            result.setSucceed(false);
            result.setMsgError("!!! Error in settings file. Big blind must have positive value !!!");
            return result;
        }

        if(smallBlindValue < 0){
            result.setSucceed(false);
            result.setMsgError("!!! Error in settings file. Small blind must have positive value !!!");
            return result;
        }

        result = isBlindLogicValid();
        return result;
    }

    private ActionResult isBlindLogicValid(){
        ActionResult result = new ActionResult(true,"");

        if(this.bigBlindValue <= this.smallBlindValue) {
            result.setSucceed(false);
            result.setMsgError("!!! Error in settings file. Big blind must be bigger the small blind !!!");
            return result;
        }
        if(!isBlindFixed){

            if(blindAddition < 0){
                result.setSucceed(false);
                result.setMsgError("!!! Error in settings file. blind addition value must be positive !!!");
                return result;
            }

            bigBlindMaxValue = calcBigBlindMaxValue();
            if(bigBlindMaxValue > buyValue / 2){
                result.setSucceed(false);
                result.setMsgError("!!! Error in settings file. Big blind max value must be lower then half of the buy value !!!");
            }
        }
        return result;
    }

    private int calcBigBlindMaxValue() {
        int totalRounds = handsCount / numberOfPlayers;
        int maxValue1 = totalRounds * blindAddition;
        int maxValue2 = blindMaxTotalRound * blindAddition;
        if (maxValue1 < maxValue2)
            return maxValue1 + bigBlindValue;
        return maxValue2 + bigBlindValue;
    }

    public ActionResult isPlayersAmountIsValid(int amount){
        switch (getGameType()){
            case Basic:{
                if(amount !=4)
                    return new ActionResult(false,"!!! Error in game settings. players amount must be 4 for game of type: basic !!!");
                break;
            }
            case MultiPlayer:{
                if(amount< 3 || amount > 6)
                    return new ActionResult(false,"!!! Error in game settings. players amount must be between 3 to 6 for game of type: multiplayer !!!");
                break;
            }
            default:{
                break;
            }
        }

        ActionResult result = new ActionResult(true,"");
        if(this.handsCount < amount || this.handsCount % amount != 0) {
            result.setSucceed(false);
            result.setMsgError("!!! Error in players amount./n Amount must be less then " + this.handsCount + "and be one of it's dividers !!! ");
        }
        return result;
    }

    private ActionResult loadPredefinedPlayers(){
        if(definedPlayers == null)
            return new ActionResult(false,"!!! Error in game settings. There are no defined players found in the settings file !!! ");

        fixedPlayersRegistration = new ArrayList<>(definedPlayers.size());
        HashSet<Integer> playerIds = new HashSet<Integer>();
        for(Player player : definedPlayers){
            fixedPlayersRegistration.add(new PlayerRegistration(player.getId().intValue(),PlayerTypes.valueOf(player.getType()) ,player.getName()));
            playerIds.add((int)player.getId().intValue());
        }
        if(playerIds.size() != fixedPlayersRegistration.size())
            return new ActionResult(false,"!!! Error in game settings. Players id's must be unique !!!");

        numberOfPlayers = fixedPlayersRegistration.size();

        return new ActionResult(true,"");
    }


    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }
}
