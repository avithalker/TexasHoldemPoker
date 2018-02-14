package Business;


import Common.ActionResult;
import Common.GlobalDefines.BetRoundTitles;
import Common.GlobalDefines.GameTypes;
import Common.GlobalDefines.PokerAction;
import Common.GlobalDefines.PokerTitle;
import Common.PlayerUtilities.*;
import Common.Replay.HandReplay;
import Common.gameExceptions.InvalidOperationException;
import Players.Player;
import Players.PlayerHandOdds;
import StateMachine.MachineStates.GameStates;
import StateMachine.MachineStates.HandRoundStates;
import StateMachine.Processors.GameStateProcessor;
import StateMachine.Processors.HandRoundStateProcessor;
import StateMachine.Processors.MachineProcessor;
import Tools.CardsDeck;
import Tools.Table;
import Utilities.XmlLoader;
import com.rundef.poker.Card;
import com.rundef.poker.EquityCalculator;
import generatedJaxb.GameDescriptor;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameManager {

    private Table gameTable;
    private CardsDeck cardsDeck;
    private Player[] pokerPlayers;
    private Player [] activePokerPlayers;
    private GameSettings gameSettings;
    private int dealerPlace;
    private int handRoundNo;
    private GambleRoundManager gambleRoundManager;
    private boolean isGambleRoundDone;
    private MachineProcessor gameStateProcessor;
    private MachineProcessor handRoundStateProcessor;
    private EquityCalculator handCalculator;
    private Date startTime;
    private HandReplay replayManager;
    private int dealerFirstPlace;


    public GameManager(){
        initializeGame();
    }

    public ActionResult startNewGame(){

        if(!checkIsNextStateValid(GameStates.GameStarted)){
            return new ActionResult(false,"Starting a new game is not a valid operation in this part of the game.");
        }

        gameTable = new Table();
        cardsDeck = new CardsDeck();
        gambleRoundManager = new GambleRoundManager();
        handCalculator = new EquityCalculator();

        gameStateProcessor.moveToNextState();
        handRoundStateProcessor.moveToNextState();
        startTime = new Date();
        return new ActionResult(true,"");
    }

    public ActionResult canStartNewHand(){
        if (handRoundStateProcessor.isStateMachineDone()) // init the stateMachine if already done.
            handRoundStateProcessor.ForceNextState(HandRoundStates.GameStarted);

        if (!checkIsNextStateValid(HandRoundStates.HandRoundStarted)) {
            return new ActionResult(false, "!!! Start new round is not a valid operation in this part of the game !!!");
        }
        return new ActionResult(true,"");
    }

    public ActionResult startNewHandRound() {

        if (handRoundStateProcessor.isStateMachineDone()) // init the stateMachine if already done.
            handRoundStateProcessor.ForceNextState(HandRoundStates.GameStarted);

        if (!checkIsNextStateValid(HandRoundStates.HandRoundStarted)) {
            return new ActionResult(false, "!!! Start new round is not a valid operation in this part of the game !!!");
        }

        if (handRoundNo >= gameSettings.getHandsCount()) {
            return new ActionResult(false, "!!! Cant start new round. Max hand rounds exceeded !!!");
        }

        initializeActivePlayers();
        if(activePokerPlayers.length <= 1)
            return new ActionResult(false,"!!! Cant start new round. There are less then 2 players that can play !!!");

        gameTable.initNewHandRound();
        cardsDeck.initNewHandRound();
        gambleRoundManager.startNewHand();

        for (Player pokerPlayer : activePokerPlayers) {
            pokerPlayer.initNewHandRound(cardsDeck.getPlayerHandCards());
        }

        handRoundNo++;
        if (handRoundNo > 1)
            forwardTitles();

        takeBlinds();

        handRoundStateProcessor.moveToNextState();
        replayManager.clearReplay();
        try {
            replayManager.setActivePlayers(getActivePlayersStatus());
            replayManager.setAllPlayers(getPlayersStatus());
            replayManager.setTableInfo(gameTable.getTableView());
        }catch (InvalidOperationException ex){}

        return new ActionResult(true, "");
    }

    public void startNewGambleRound() {
        BetRoundTitles betRoundTitle = gambleRoundManager.startNewGambleRound(activePokerPlayers, dealerPlace);
        if (betRoundTitle != BetRoundTitles.None) {
            isGambleRoundDone = false;
            handleNewGambleRoundStart(betRoundTitle);
            try {
                replayManager.addNewRoundReplay(betRoundTitle,gameTable.getTableView(),getActivePlayersStatus());
            }
            catch (InvalidOperationException ex){}
        }
    }

    public List<WinnerInfo> finishHandAndGetWinners() throws InvalidOperationException{
        if(!handRoundStateProcessor.isStateMachineDone())
            throw new InvalidOperationException("Cant call finishHand function because the hand round is not over yet!");

        ArrayList<Player> activePlayers = new ArrayList<>();

        for (Player player:activePokerPlayers) {
            if (player.getLastAction() != PokerAction.FOLD)
                activePlayers.add(player);
        }

        ArrayList<WinnerInfo> winners = getWinners(activePlayers);
        updateWinners(winners);
        dropActivePlayersCards();
        return winners;
    }

    public void loadGameSettingsAsync(LoadSettingsTask loadSettingsTask,String settingsFilePath) throws InvalidOperationException{
        File fileMetaData = new File(settingsFilePath);
        if(gameStateProcessor.isCurrStateBiggerThen(GameStates.PlayerRegisterd)){
            throw new InvalidOperationException("Loading setting is not a valid operation in this part of the game");
        }
        if(!fileMetaData.exists() || !fileMetaData.isFile()){
            throw new InvalidOperationException("Error in loading the settings file\nFile not exist");
        }
        if(!settingsFilePath.endsWith(".xml")) {
            throw new InvalidOperationException("Error in loading the settings file\nFile is not .xml type");
        }

        if(gameStateProcessor.isCurrentSateEquals(GameStates.PlayerRegisterd)) // if we loaded old settings before
            gameStateProcessor.ForceNextState(GameStates.None);

        loadSettingsTask.setSettingsFilePath(settingsFilePath);
        new Thread(loadSettingsTask).start();
    }

    public void setGameSettings(GameSettings gameSettings){
        this.gameSettings = gameSettings;
    }

    public GameSettings getGameSettings(){
        return gameSettings;
    }

    public LoadSettingsTask getAsyncSettingLoaderObject(){
        LoadSettingsTask loadSettingsTask = new LoadSettingsTask(this,gameStateProcessor);
        return loadSettingsTask;
    }

    public ActionResult loadGameSettings(String settingsFilePath){
        File fileMetaData = new File(settingsFilePath);

        if(gameStateProcessor.isCurrStateBiggerThen(GameStates.PlayerRegisterd)){
            return new ActionResult(false,"!!! Loading setting is not a valid operation in this part of the game !!!");
        }
        if(!fileMetaData.exists() || !fileMetaData.isFile()){
            return new ActionResult(false,"!!! Error in loading the settings file !!!\nFile not exist");
        }
        if(!settingsFilePath.endsWith(".xml")) {
            return new ActionResult(false, "!!! Error in loading the settings file !!!\nFile is not .xml type");
        }

        if(gameStateProcessor.isCurrentSateEquals(GameStates.PlayerRegisterd)) // if we loaded old settings before
            gameStateProcessor.ForceNextState(GameStates.None);

        try {
            GameDescriptor settingsDescriptor = XmlLoader.LoadGameSettings(settingsFilePath);
            GameSettings gameSettings = convertDescriptorToSettings(settingsDescriptor);
            ActionResult result = gameSettings.isGeneralSettingsValid();

            if(result.isSucceed()){
                this.gameSettings = gameSettings;
                gameStateProcessor.moveToNextState();
                if(gameSettings.getGameType() == GameTypes.MultiPlayer)
                    result = registerPlayers(gameSettings.getFixedPlayersRegistration(),true);
            }

            return result;
        }
        catch (JAXBException e)
        {
            return new ActionResult(false,"!!! Error has occurred during the settings loading. please check the file !!!");
        }
        catch (Exception e){
            return new ActionResult(false,"Error has occurred during the settings loading. seems like some fields are missing in the file!");
        }

    }

    public ActionResult loadGameSettingsFromString(String settingContent){
        if(gameStateProcessor.isCurrStateBiggerThen(GameStates.PlayerRegisterd)){
            return new ActionResult(false,"!!! Loading setting is not a valid operation in this part of the game !!!");
        }

        if(gameStateProcessor.isCurrentSateEquals(GameStates.PlayerRegisterd)) // if we loaded old settings before
            gameStateProcessor.ForceNextState(GameStates.None);

        try {
            GameDescriptor settingsDescriptor = XmlLoader.LoadGameSettingsFromStr(settingContent);
            GameSettings gameSettings = convertDescriptorToSettings(settingsDescriptor);
            ActionResult result = gameSettings.isGeneralSettingsValid();

            if(result.isSucceed()){
                this.gameSettings = gameSettings;
                gameStateProcessor.moveToNextState();
                if(gameSettings.getGameType() == GameTypes.MultiPlayer)
                    result = registerPlayers(gameSettings.getFixedPlayersRegistration(),true);
            }

            return result;
        }
        catch (JAXBException e)
        {
            return new ActionResult(false,"!!! Error has occurred during the settings loading. please check the file !!!");
        }
        catch (Exception e){
            return new ActionResult(false,"Error has occurred during the settings loading. seems like some fields are missing in the file!");
        }

    }

    public ArrayList<WinnerInfo> killHand(int killerId){
        endHandRound();
        isGambleRoundDone = true;

        ArrayList<WinnerInfo> winners = new ArrayList<>();

        for (Player player:activePokerPlayers) {
            if (player.getLastAction() != PokerAction.FOLD && player.getPlayerId() != killerId)
                winners.add(new WinnerInfo(player.getPlayerId(),""));
        }

        updateWinners(winners);
        dropActivePlayersCards();
        return winners;
    }

    private void dropActivePlayersCards(){
        for(Player player:activePokerPlayers){
            player.dropCards();
        }
    }
    public void skipAllGambleRounds(){
        BetRoundTitles betRoundTitle;
        do {
            betRoundTitle = gambleRoundManager.startNewGambleRound(activePokerPlayers, dealerPlace);
            if (betRoundTitle != BetRoundTitles.None) {
                isGambleRoundDone = false;
                handleNewGambleRoundStart(betRoundTitle);
                try {
                    replayManager.addNewRoundReplay(betRoundTitle,gameTable.getTableView(),getActivePlayersStatus());
                }
                catch (InvalidOperationException ex){
                    System.out.println(ex.getMessage());
                }

            }
        } while (betRoundTitle != BetRoundTitles.None && betRoundTitle != BetRoundTitles.River); // this will expose all cards!
        endHandRound();
        isGambleRoundDone = true;
    }

    public PlayerInfo[] getPlayersStatus() throws InvalidOperationException{

        if(!gameStateProcessor.isSubStateCanBeExecute(GameStates.GetGameStatus))
            throw new InvalidOperationException("!!! Cant show players status in this part of the game !!!");

        PlayerInfo[] playersStatus = new PlayerInfo[pokerPlayers.length];
        for(int i = 0;i<pokerPlayers.length; i++){
            playersStatus[i] = pokerPlayers[i].getPlayerInfo();
        }
        return playersStatus;
    }

    public PlayerInfo[] getActivePlayersStatus() throws InvalidOperationException{

        if(!gameStateProcessor.isSubStateCanBeExecute(GameStates.GetGameStatus))
            throw new InvalidOperationException("!!! Cant show players status in this part of the game !!!");

        PlayerInfo[] playersStatus = new PlayerInfo[activePokerPlayers.length];
        for(int i = 0;i<activePokerPlayers.length; i++){
            playersStatus[i] = activePokerPlayers[i].getPlayerInfo();
        }
        return playersStatus;
    }

    public boolean isAllActivePlayerHasTokens(){
        for(Player player: activePokerPlayers){
            if(player.getLastAction() != PokerAction.FOLD && player.getTokens() == 0)
                return false;
        }
        return true;
    }

    public boolean isAllHandsDone(){
        if (handRoundNo >= gameSettings.getHandsCount()) {
            return true;
        }
        return false;
    }

    public void forceInitializeNewGame(){
        gameStateProcessor = new GameStateProcessor();
        handRoundStateProcessor = new HandRoundStateProcessor();
        replayManager = new HandReplay();
        handRoundNo = 0;
    }

    public GameGeneralInfo getGameGeneralInfo() throws InvalidOperationException{
        if (!gameStateProcessor.isSubStateCanBeExecute(GameStates.GetGameStatus))
            throw new InvalidOperationException("!!! Cant show game statistics in this part of the game !!!");

        GameGeneralInfo generalInfo = new GameGeneralInfo();
        generalInfo.setBuyValue(gameSettings.getBuyValue());
        generalInfo.setBigBlindValue(getBigBlindValue());
        generalInfo.setSmallBlindValue(getSmallBlindValue());
        generalInfo.setBlindAddition(gameSettings.getBlindAddition());
        generalInfo.setBlindFixed(gameSettings.isBlindFixed());
        generalInfo.setHandsCount(gameSettings.getHandsCount());
        generalInfo.setTotalTokens(getToalTokensInGame());
        generalInfo.setTotalPlayedHands(handRoundNo);

        return generalInfo;
    }

    public GameStatistics getGameStatistics() throws InvalidOperationException {
        if (!gameStateProcessor.isSubStateCanBeExecute(GameStates.GetGameStatus))
            throw new InvalidOperationException("!!! Cant show game statistics in this part of the game !!!");
        GameStatistics statistics = new GameStatistics();

        statistics.setPlayerStatuses(getPlayersStatus());
        statistics.setToalHandGames(gameSettings.getHandsCount());
        statistics.setTotalPlayedHands(handRoundNo);
        statistics.setTotalTokens(getToalTokensInGame());
        long timePastInSec = 0;
        if(startTime != null)
            timePastInSec = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - startTime.getTime());
        statistics.setTimePastInSec(timePastInSec);
        return statistics;
    }

    public int getBigBlindValue(){
        if(gameSettings != null)
            return gameSettings.getBigBlindValue();
        return 0;
    }

    public int getSmallBlindValue(){
        if(gameSettings != null)
            return gameSettings.getSmallBlindValue();
        return 0;
    }

    public boolean isHandRoundEnded() {
        return gambleRoundManager.isHandDone(activePokerPlayers, gameTable.getBetSize());
    }

    public boolean isAnActivePlayer(int playerId){
        return isAnActivePlayer(getPlayerById(playerId));
    }

    public boolean isPlayerFold(int playerId){
        for(Player player : activePokerPlayers){
            if(player.getPlayerId() == playerId){
                if(player.getLastAction() == PokerAction.FOLD)
                    return true;
                else
                    return false;
            }
        }
        return true;
    }

    public boolean isHasMoreHands(){
        if(gameSettings != null)
            return handRoundNo < gameSettings.getHandsCount();
        return true;
    }

    public ActionResult registerPlayers(ArrayList<PlayerRegistration> playerRegistrations, boolean randomInitialization) {

        ActionResult result;

        if (!checkIsNextStateValid(GameStates.PlayerRegisterd)) {
            return new ActionResult(false, "!!! registering players is not a valid operation in this part of the game !!!");
        }

        result = this.gameSettings.isPlayersAmountIsValid(playerRegistrations.size());
        if (!result.isSucceed())
            return result;

        pokerPlayers = new Player[playerRegistrations.size()];

        int fixedIndex = 0;
        for (PlayerRegistration registration : playerRegistrations) {
            Player newPlayer = new Player(registration.getPlayerId(), registration.getPlayerType(),
                    gameSettings.getBuyValue(),registration.getPlayerName());

            if(randomInitialization)
                this.pokerPlayers[getRandomPlaceForPlayer()] = newPlayer;
            else
            {
                this.pokerPlayers[fixedIndex] = newPlayer;
                fixedIndex++;
            }
        }
        initializePlayersTitles(randomInitialization);
        gameStateProcessor.moveToNextState();
        return result;
    }

    public PlayerInfo getCurrentPlayerInfo(){
        int playerPlace = this.gambleRoundManager.getCurrPlayerPlace();
        return this.activePokerPlayers[playerPlace].getPlayerInfo();
    }

    public PlayerInfo getPlayerInfoById(int playerId){
        for(Player player: pokerPlayers){
            if(player.getPlayerId() == playerId)
                return player.getPlayerInfo();
        }
        return null;
    }

    public PokerTableView getTableView(){
        PokerTableView tableView = gameTable.getTableView();
        tableView.setSmallValue(gameSettings.getSmallBlindValue());
        tableView.setBigValue(gameSettings.getBigBlindValue());
        tableView.setFirstBetReceived(gambleRoundManager.isFirstBetReceived());
        int poorestPlayerPot = getPoorestPlayerPot();
        if(poorestPlayerPot < gameTable.getPot())
            tableView.setMaxBetValue(poorestPlayerPot);
        else
            tableView.setMaxBetValue(gameTable.getPot());
        return tableView;
    }

    public boolean isGambleRoundDone() {
        return isGambleRoundDone;
    }

    public boolean isPlayerTurnValidation(String playerName){
        Player player = findPlayerByName(playerName);
        if(player == null)
            return false;
        ActionResult result = isPlayerTurnValidation(player.getPlayerId());
        return result.isSucceed();
    }

    public ActionResult isPokerActionValid(int playerId, PokerAction action){
        Player requstedPlayer = getPlayerById(playerId);
        ActionResult turnResult = isPlayerTurnValidation(playerId);
        if(!turnResult.isSucceed())
            return turnResult;
        if(!handRoundStateProcessor.isCurrentSateEquals(HandRoundStates.HandRoundStarted))
            return new ActionResult(false,"!!! Error- player cant make any action right now until the hand will be reInitialized !!!");

        if(action == PokerAction.FOLD) // Fold is always allowed
            return new ActionResult(true,"");
        if(action == PokerAction.CHECK && requstedPlayer.getCurrentBet() == gameTable.getBetSize()) // check can be done only when bet equals to bet size
            return new ActionResult(true,"");
        if(action == PokerAction.BET && gambleRoundManager.isFirstBetReceived()) // Bet is allowed only once in a round
            return new ActionResult(false,"!!! Invalid action. Bet action is allowed only once in a round !!!");
        if(action != PokerAction.BET && !gambleRoundManager.isFirstBetReceived()) // Bet must be the first action
            return new ActionResult(false,"!!! Invalid action. Bet must be the first action in the new round !!!");
        if(action == PokerAction.CHECK && requstedPlayer.getCurrentBet() != gameTable.getBetSize()) // check can be done only when bet equals to bet size
            return new ActionResult(false,"!!! Invalid action. Check can be done only when your bet is equals to the minimum bet size !!!");
        if(action == PokerAction.CALL && requstedPlayer.getCurrentBet() >= gameTable.getBetSize())
            return new ActionResult(false,"!!! Invalid action. Call action can't be made because player bet is higher then the table bet size !!!");
        if(action == PokerAction.CALL && gameTable.getBetSize() - requstedPlayer.getCurrentBet() > requstedPlayer.getTokens())
            return new ActionResult(false,"!!! Invalid action. Call action can't be made. insufficient funds !!!");

        return new ActionResult(true,"");
    }

    public HashMap<PokerAction,Boolean> getValidPokerActionsForPlayer(int playerId){
        HashMap<PokerAction,Boolean> actionValidation = new HashMap<>();
        actionValidation.put(PokerAction.CALL,false);
        actionValidation.put(PokerAction.FOLD,false);
        actionValidation.put(PokerAction.CHECK,false);
        actionValidation.put(PokerAction.BET,false);
        actionValidation.put(PokerAction.RAISE,false);
        for (PokerAction pokerAction:actionValidation.keySet()){
            if(isPokerActionValid(playerId,pokerAction).isSucceed())
                actionValidation.replace(pokerAction,true);
        }
        return actionValidation;
    }

    public HandReplay getGameReplay(){
        return replayManager;
    }

    public ActionResult buyTokens(int playerId){
        if(!gameStateProcessor.isCurrStateBiggerThen(GameStates.PlayerRegisterd))
            return new ActionResult(false,"!!! Error- buy tokens is not a valid operation in this part of the game !!!");
        Player player = getPlayerById(playerId);
        player.addBuy(gameSettings.getBuyValue(),1);

        return new ActionResult(true,"");
    }

    public ActionResult buyTokens(String playerName){
        for(int i=0;i<pokerPlayers.length;i++){
            if(pokerPlayers[i].getPlayerName() == playerName){
                return buyTokens(pokerPlayers[i].getPlayerId());
            }
        }
        return new ActionResult(false,"Error- player not found");
    }

    public ActionResult foldFromEntireGame(int playerId){
        if(!gameStateProcessor.isCurrStateBiggerThen(GameStates.PlayerRegisterd))
            return new ActionResult(false,"The game must start in order to make this action.");
        Player selectedPlayer = getPlayerById(playerId);
        selectedPlayer.setPlayerFoldFromEntireGame(true);
        return new ActionResult(true,"");
    }
    public ActionResult Fold(int playerId) {
        ActionResult result = isPokerActionValid(playerId,PokerAction.FOLD);
        if(!result.isSucceed())
            return result;

        Player currPlayer =  getCurrentPlayer();
        currPlayer.setLastAction(PokerAction.FOLD);
        replayManager.addNewPlayerAction(currPlayer.getPlayerInfo(),gameTable.getPot(),gameTable.getBetSize());
        if(!checkIfGambleRoundEnded())
            gambleRoundManager.moveToNextPlayer(activePokerPlayers);
        return result;
    }

    public ActionResult Bet(int playerId, int bet) {
        ActionResult result = isPokerActionValid(playerId,PokerAction.BET);
        if(!result.isSucceed())
            return result;

        Player currPlayer =  getCurrentPlayer();

        result = isAllHaveEnoughTokensValidation(bet);
        if(!result.isSucceed())
            return result;
        if(bet > gameTable.getPot())
            return new ActionResult(false,"!!! Error- Bet value is bigger then pot value which is not allowed on pot limit mode !!!");
        if(bet <= 0)
            return new ActionResult(false,"Error- bet value must be positive");
        if(bet + currPlayer.getCurrentBet() < gameTable.getBetSize()){
            return new ActionResult(false,"!!! Error- Bet value must be bigger then the current bet size (big blind) !!!");
        }

        result = currPlayer.Bet(bet);
        if(!result.isSucceed())
            return result;
        currPlayer.setLastAction(PokerAction.BET);
        gambleRoundManager.setFirstBetReceived(true, currPlayer);
        gameTable.increasePot(bet);
        gameTable.updateBetSize(currPlayer.getCurrentBet());
        replayManager.addNewPlayerAction(currPlayer.getPlayerInfo(),gameTable.getPot(),gameTable.getBetSize());
        gambleRoundManager.moveToNextPlayer(activePokerPlayers);
        return result;
    }

    public ActionResult Call(int playerId) {
        ActionResult result = isPokerActionValid(playerId,PokerAction.CALL);
        if(!result.isSucceed())
            return result;

        Player currPlayer =  getCurrentPlayer();
        int playerPrevBet = currPlayer.getCurrentBet();
        result = currPlayer.Call(gameTable.getBetSize());
        if(!result.isSucceed())
            return result;

        currPlayer.setLastAction(PokerAction.CALL);
        gameTable.increasePot((currPlayer.getCurrentBet() - playerPrevBet));
        replayManager.addNewPlayerAction(currPlayer.getPlayerInfo(),gameTable.getPot(),gameTable.getBetSize());
        if(!checkIfGambleRoundEnded())
            gambleRoundManager.moveToNextPlayer(activePokerPlayers);
        return result;
    }

    public ActionResult Check(int playerId) {
        ActionResult result = isPokerActionValid(playerId,PokerAction.CHECK);
        if(!result.isSucceed())
            return result;

        Player currPlayer =  getCurrentPlayer();
        currPlayer.Check();
        currPlayer.setLastAction(PokerAction.CHECK);
        replayManager.addNewPlayerAction(currPlayer.getPlayerInfo(),gameTable.getPot(),gameTable.getBetSize());
        if(!checkIfGambleRoundEnded())
            gambleRoundManager.moveToNextPlayer(activePokerPlayers);
        return result;
    }


    public ActionResult Raise(int playerId, int raise) {
        ActionResult result = isPokerActionValid(playerId,PokerAction.RAISE);
        if(!result.isSucceed())
            return result;

        result = isAllHaveEnoughTokensValidation(gameTable.getBetSize()+raise);
        if(!result.isSucceed())
            return result;
        if(raise > gameTable.getPot())
            return new ActionResult(false,"!!! Error- raise value is bigger then pot value which is not allowed on pot limit mode !!!");

        Player currPlayer =  getCurrentPlayer();
        int playerPrevBet = currPlayer.getCurrentBet();
        result = currPlayer.Raise(gameTable.getBetSize(), raise);
        if(!result.isSucceed())
            return result;

        currPlayer.setLastAction(PokerAction.RAISE);
        gambleRoundManager.setLastRaisedPlayer(currPlayer);
        gameTable.increasePot((currPlayer.getCurrentBet() - playerPrevBet));
        gameTable.updateBetSize(currPlayer.getCurrentBet());
        replayManager.addNewPlayerAction(currPlayer.getPlayerInfo(),gameTable.getPot(),gameTable.getBetSize());
        if(!checkIfGambleRoundEnded())
            gambleRoundManager.moveToNextPlayer(activePokerPlayers);
        return result;
    }

    public int findPlayerIdByName(String playerName){
        Player player = findPlayerByName(playerName);
        if(player == null)
            return -1;
        return player.getPlayerId();
    }

    private Player findPlayerByName(String playerName){
        for(Player player:pokerPlayers){
            if(player.getPlayerName() == playerName)
                return player;
        }
        return null;
    }

    private void updateWinners(ArrayList<WinnerInfo>winners) {
        int winningPrice = gameTable.getPot() / winners.size();
        gameTable.setPotLastHandLeftOver(gameTable.getPot() % winners.size());
        for(WinnerInfo winner:winners) {
            PlayerInfo player = getPlayerById(winner.getPlayerId()).getPlayerInfo();
            winner.setWinningPrice(winningPrice);
            winner.setName(player.getPlayerName());
            winner.setPlayerType(player.getPlayerType());
            winner.setTotalBuys(player.getTotalBuys());
            winner.setTotalHandsPlayed(player.getTotalHandsPlayed());
            winner.setTotalWinsNumber(player.getTotalWins() + 1);
        }
        winners.forEach(w -> getPlayerById(w.getPlayerId()).notifyOnWin(winningPrice));
    }

    private ArrayList<WinnerInfo> getWinners(ArrayList<Player> activePlayers) throws InvalidOperationException{
        ArrayList<PlayerHandOdds> handOdds = new ArrayList<>();
        ArrayList<WinnerInfo> winners = new ArrayList<>();

        if(activePlayers.size() == 1){
            winners.add(new WinnerInfo(activePlayers.get(0).getPlayerId(),"Last player standing"));
            return winners;
        }

        handCalculator.reset();
        try {
            handCalculator.setBoard(gameTable.getTableCards());
            activePlayers.forEach(p-> handCalculator.addHand(p.getHand()));
            handCalculator.calculate();
            List<Integer> winnerHandIndexs = handCalculator.getWinningHands();

            for (int i = 0; i< activePlayers.size(); i++){
                handOdds.add(new PlayerHandOdds(activePlayers.get(i).getPlayerId(),
                        handCalculator.getHandEquity(i).getEquity(),
                        handCalculator.getHandRanking(i).toString()));
            }
            //Collections.sort(handOdds,Collections.reverseOrder());

            for(Integer i: winnerHandIndexs){
                winners.add(new WinnerInfo(handOdds.get(i).getPlayerId(),handOdds.get(i).getHandRank()));
            }

            return winners;
        }
        catch (Exception e) {
            throw new InvalidOperationException("!!! Unexpected failure has happened during the calculation process !!!");
        }
    }

    private GameSettings convertDescriptorToSettings(GameDescriptor descriptor){
        GameSettings gameSettings = new GameSettings();

        gameSettings.setGameType(descriptor.getGameType());
        gameSettings.setHandsCount(descriptor.getStructure().getHandsCount().intValue());
        gameSettings.setBuyValue(descriptor.getStructure().getBuy().intValue());
        gameSettings.setBigBlindValue(descriptor.getStructure().getBlindes().getBig().intValue());
        gameSettings.setSmallBlindValue(descriptor.getStructure().getBlindes().getSmall().intValue());
        gameSettings.setBlindFixed(descriptor.getStructure().getBlindes().isFixed());
        if(!gameSettings.isBlindFixed()) {
            gameSettings.setBlindAddition(descriptor.getStructure().getBlindes().getAdditions().intValue());
            gameSettings.setBlindMaxTotalRound(descriptor.getStructure().getBlindes().getMaxTotalRounds().intValue());
        }
        gameSettings.setTotalPlayers(descriptor.getDynamicPlayers().getTotalPlayers());
        gameSettings.setGameTitle(descriptor.getDynamicPlayers().getGameTitle());
        ///gameSettings.setDefinedPlayers(descriptor.getPlayers().getPlayer());

        return gameSettings;
    }

    private int getRandomPlaceForPlayer()
    {
        Random randomPlace = new Random();
        int place;
        do {
            place = randomPlace.nextInt(this.pokerPlayers.length );
        }while (this.pokerPlayers[place] != null);
        return place;
    }

    private void takeBlinds(){
        for(Player player:activePokerPlayers){
            if(player.getTitle() == PokerTitle.SMALL)
                player.blindBet(gameSettings.getSmallBlindValue());
            else if(player.getTitle() == PokerTitle.BIG)
                player.blindBet(gameSettings.getBigBlindValue());
        }
        gameTable.increasePot(gameSettings.getSmallBlindValue());
        gameTable.increasePot(gameSettings.getBigBlindValue());
        gameTable.updateBetSize(gameSettings.getBigBlindValue());
    }

    private int getToalTokensInGame(){
        int totalTokenAmount = 0;
        if(pokerPlayers == null || pokerPlayers.length == 0)
            return totalTokenAmount;
        for(Player player:pokerPlayers){
            totalTokenAmount += player.getTokens();
        }
        if(gameTable != null)
            totalTokenAmount+= gameTable.getPotLastHandLeftOver();
        return totalTokenAmount;
    }

    private void initializeGame(){
        gameStateProcessor = new GameStateProcessor();
        handRoundStateProcessor = new HandRoundStateProcessor();
        replayManager = new HandReplay();
        handRoundNo = 0;
    }

    private void initializeActivePlayers(){
        ArrayList<Player> activeList = new ArrayList<>();
        for(Player player : pokerPlayers){
            if(isAnActivePlayer(player))
                activeList.add(player);
        }
        activePokerPlayers = new Player[activeList.size()];
        activePokerPlayers = activeList.toArray(activePokerPlayers);
    }

    private boolean isAnActivePlayer(Player player){
        if(player.getTokens() > 0 && player.getTokens() > gameSettings.getBigBlindValue() && !player.isPlayerFoldFromEntireGame())
            return true;
        return false;
    }


    private void initializePlayersTitles(boolean randomInitialization){
        Random rnd = new Random();
        int titlePlace;
        if(randomInitialization)
            titlePlace = rnd.nextInt(pokerPlayers.length);
        else
            titlePlace = 0;
        this.pokerPlayers[titlePlace].setTitle(PokerTitle.DEALER);
        this.dealerPlace = titlePlace;
        dealerFirstPlace = dealerPlace;
        titlePlace = (titlePlace + 1) % pokerPlayers.length;
        this.pokerPlayers[titlePlace].setTitle(PokerTitle.SMALL);
        titlePlace = (titlePlace + 1) % pokerPlayers.length;
        this.pokerPlayers[titlePlace].setTitle(PokerTitle.BIG);
    }

    private void forwardTitles(){

        for(Player player : pokerPlayers)
            player.setTitle(PokerTitle.REGULAR);
        int titlePlace = this.dealerPlace;
        do {
            titlePlace = (titlePlace + 1) % pokerPlayers.length;
            checkBlindUpdate(titlePlace);
        }while (!isAnActivePlayer(pokerPlayers[titlePlace]));
            this.pokerPlayers[titlePlace].setTitle(PokerTitle.DEALER);
        this.dealerPlace = titlePlace;

        do {
            titlePlace = (titlePlace + 1) % pokerPlayers.length;
        }while (!isAnActivePlayer(pokerPlayers[titlePlace]));
        this.pokerPlayers[titlePlace].setTitle(PokerTitle.SMALL);

        do {
            titlePlace = (titlePlace + 1) % pokerPlayers.length;
        }while (!isAnActivePlayer(pokerPlayers[titlePlace]));
        this.pokerPlayers[titlePlace].setTitle(PokerTitle.BIG);
    }

    private void checkBlindUpdate(int newDealerPlace){
        if(gameSettings.isBlindFixed())
            return;
        if(newDealerPlace != dealerFirstPlace)
            return;
        if(gameSettings.getBigBlindValue() == gameSettings.getBigBlindMaxValue())
            return;
        if(gameSettings.getBigBlindValue() + gameSettings.getBlindAddition() <= gameSettings.getBigBlindMaxValue())
            gameSettings.setBigBlindValue(gameSettings.getBigBlindValue() + gameSettings.getBlindAddition());
        else
            gameSettings.setBigBlindValue(gameSettings.getBigBlindMaxValue());

        gameSettings.setSmallBlindValue(gameSettings.getSmallBlindValue() + gameSettings.getBlindAddition());
    }

    private boolean checkIsNextStateValid(GameStates nextState){
        return gameStateProcessor.isNextStateValid(nextState);
    }

    private boolean checkIsNextStateValid(HandRoundStates nextState){
        return handRoundStateProcessor.isNextStateValid(nextState);
    }

    private boolean checkIfGambleRoundEnded(){
        if(gambleRoundManager.isGambleRoundDone(activePokerPlayers, gameTable.getBetSize())){
            if(gambleRoundManager.isHandDone(activePokerPlayers, gameTable.getBetSize())) // check if whole hand finished
                endHandRound();
            isGambleRoundDone = true;
            return true;
        }
        return false;
    }

    private void endHandRound(){
        if(checkIsNextStateValid(HandRoundStates.HandRoundEnded))
            handRoundStateProcessor.moveToNextState();
    }

    private void handleNewGambleRoundStart(BetRoundTitles betRoundTitle) {
        switch (betRoundTitle) {
            case FirstBetRound:
                break;
            case Flop: {
                Card[] cards = cardsDeck.getFlopCards();
                for(Card card:cards)
                    gameTable.exposeNewCard(card);
                break;
            }
            case Turn:
            case River: {
                gameTable.exposeNewCard(cardsDeck.getCard());
                break;
            }
            case None:{
                break;
            }
        }
        if(betRoundTitle != BetRoundTitles.FirstBetRound && betRoundTitle != BetRoundTitles.None){
            initPlayersForGambleRound();
            gameTable.updateBetSize(0);
        }
    }

    private void initPlayersForGambleRound() {
        for (Player player : activePokerPlayers)
            if(player.getLastAction() != PokerAction.FOLD)
                player.initNewGambleRound();
    }

    private Player getPlayerById(int playerId){
        for(Player player: pokerPlayers){
            if(player.getPlayerId() == playerId)
                return player;
        }
        return null;
    }

    private Player getCurrentPlayer(){
        int playerPlace = this.gambleRoundManager.getCurrPlayerPlace();
        return this.activePokerPlayers[playerPlace];
    }

    private ActionResult isPlayerTurnValidation(int playerId){
        if(getCurrentPlayer().getPlayerId() != playerId)
            return new ActionResult(false,"!!! Error- This is not your turn !!!");
        return new ActionResult(true,"");
    }

    private ActionResult isAllHaveEnoughTokensValidation(int newBetSize){
        for(Player player:activePokerPlayers){
            if(player.getLastAction()!=PokerAction.FOLD && newBetSize - player.getCurrentBet() > player.getTokens())
                return new ActionResult(false,"!!! Error- not all players can put this amount of tokens !!!");
        }
        return new ActionResult(true,"");
    }

    private int getPoorestPlayerPot(){
        int min = -1;
        for(Player player: activePokerPlayers){
            if(player.getLastAction() != PokerAction.FOLD){
                if(min == -1)
                    min = player.getTokens();
                else
                {
                    if(min > player.getTokens())
                        min = player.getTokens();
                }
            }
        }
        return min;
    }
}
