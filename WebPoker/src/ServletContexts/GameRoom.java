package ServletContexts;

import Business.GameManager;
import Business.GameSettings;
import Common.ActionResult;
import Common.ExternalPlayer.ComputerPlayer;
import Common.ExternalPlayer.IExternalPlayer;
import Common.GlobalDefines.PlayerTypes;
import Common.GlobalDefines.PokerAction;
import Common.GlobalDefines.RoomStatuses;
import Common.PlayerUtilities.*;
import Common.gameExceptions.InvalidOperationException;
import PokerDtos.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameRoom {

    private GameRoomUserManager roomUserManager;
    private GameManager gameManager;
    private RoomStatuses gameStatus;
    private String gameEndReason = "";
    private String handEndReason= "";
    private boolean isFirstHandStarted;
    private String roomOwner;
    private int roomMaxCapacity;
    private List<WinnerInfo> winners;

    public GameRoom(String roomOwner){
        this.roomOwner = roomOwner;
        roomMaxCapacity = 0;
        isFirstHandStarted = false;
        gameStatus = RoomStatuses.Pending;
        roomUserManager = new GameRoomUserManager();
        gameManager = new GameManager();
    }

    private void initRoomForNewGame() {
        isFirstHandStarted = false;
        gameStatus = RoomStatuses.Pending;
        gameEndReason = "";
        handEndReason = "";
        ActionResult result = gameManager.forceInitializeNewGameWithSameSettings();
        if (!result.isSucceed())
            System.out.println("ERROR- FAILED TO INITIALIZE NEW GAME!");
    }

    public boolean joinRoom(PlayerSignInDto playerDetails){

        return roomUserManager.addUser(playerDetails);
    }

    public void leaveRoom(String playerName){
        roomUserManager.removeUser(playerName);
        if(gameStatus == RoomStatuses.Active){
            gameManager.foldFromEntireGame(gameManager.findPlayerIdByName(playerName));
            if (roomUserManager.isAllReady()) {
                startHand();
            }
        }
        else if(gameStatus == RoomStatuses.End){
            if(roomUserManager.getUsersCount() == 0){
                initRoomForNewGame();
            }
        }
    }

    public boolean setPlayerIsReadyStatus(String playerName,boolean isReady) {
        if(gameStatus == RoomStatuses.End)
            return false;
        roomUserManager.setReadyStatus(playerName, isReady);
        if(gameStatus == RoomStatuses.Active) {
            if (roomUserManager.isAllReady()) {
                startHand();
            }
        }

        return true;
    }

    public synchronized ActionResult loadRoomSettings(String settings){
        ActionResult result = gameManager.loadGameSettingsFromString(settings);
        if(result.isSucceed()) {
            GameSettings gameSettings = gameManager.getGameSettings();
            roomMaxCapacity = gameSettings.getTotalPlayers();
        }
        return result;
    }

    public RoomStatuses getGameStatus(){
        return gameStatus;
    }

    public GameRoomDto getRoomDetails(){

        GameRoomDto roomDetails = new GameRoomDto();
        GameSettings settings = gameManager.getGameSettings();
        roomDetails.setOwner(roomOwner);
        roomDetails.setBigBlind(settings.getBigBlindValue());
        roomDetails.setBlindAddition(settings.getBlindAddition());
        roomDetails.setBuySize(settings.getBuyValue());
        roomDetails.setHandsCount(settings.getHandsCount());
        roomDetails.setSmallBlind(settings.getSmallBlindValue());
        roomDetails.setIsBlindFixed(settings.isBlindFixed());
        roomDetails.setGameStatus(gameStatus.toString());
        roomDetails.setRegisteredPlayers(roomUserManager.getUsersCount());
        roomDetails.setGameTitle(settings.getGameTitle());
        roomDetails.setMaxPlayers(settings.getTotalPlayers());

        return  roomDetails;
    }

    public GameGeneralInfoDto getGameGeneralInfo() throws InvalidOperationException{
        GameGeneralInfoDto gameGeneralInfoDto = new GameGeneralInfoDto();
        GameGeneralInfo gameGeneralInfo = gameManager.getGameGeneralInfo();

        gameGeneralInfoDto.setBigBlindValue(gameGeneralInfo.getBigBlindValue());
        gameGeneralInfoDto.setBlindAddition(gameGeneralInfo.getBlindAddition());
        gameGeneralInfoDto.setBlindFixed(gameGeneralInfo.isBlindFixed());
        gameGeneralInfoDto.setBuyValue(gameGeneralInfo.getBuyValue());
        gameGeneralInfoDto.setHandsCount(gameGeneralInfo.getHandsCount());
        gameGeneralInfoDto.setSmallBlindValue(gameGeneralInfo.getSmallBlindValue());
        gameGeneralInfoDto.setTotalPlayedHands(gameGeneralInfo.getTotalPlayedHands());
        gameGeneralInfoDto.setTotalTokens(gameGeneralInfo.getTotalTokens());

        return gameGeneralInfoDto;
    }

    public SimpleResultDto isComputerType(String playerName){
        PlayerSignInDto playerInfo = roomUserManager.getUser(playerName);
        SimpleResultDto resultDto = new SimpleResultDto(playerInfo.isComputer());
        return resultDto;
    }

    public PlayerInfoDto[] getPlayersInfo() throws InvalidOperationException{
        PlayerInfo[] playersInfo = gameManager.getPlayersStatus();
        PlayerInfoDto[] playersInfoDto = new PlayerInfoDto[playersInfo.length];

        int i = 0;
        for(PlayerInfo player : playersInfo){
            playersInfoDto[i] = new PlayerInfoDto();
            playersInfoDto[i].setPlayerName(player.getPlayerName());
            playersInfoDto[i].setPlayerType(player.getPlayerType().toString());
            playersInfoDto[i].setTokens(player.getTokens());
            playersInfoDto[i].setTotalBuys(player.getTotalBuys());
            playersInfoDto[i].setTotalHandsPlayed(player.getTotalHandsPlayed());
            playersInfoDto[i].setTotalWins(player.getTotalWins());
            i++;
        }

        return playersInfoDto;
    }

    public PlayerGameStatusDto[] getPlayersGameStatus() throws InvalidOperationException{
        PlayerInfo[] playersInfo = gameManager.getPlayersStatus();
        PlayerGameStatusDto[] playersGameStatusDto = new PlayerGameStatusDto[playersInfo.length];

        int i = 0;
        for(PlayerInfo player : playersInfo){
            playersGameStatusDto[i] = new PlayerGameStatusDto();
            playersGameStatusDto[i].setPlayerName(player.getPlayerName());
            playersGameStatusDto[i].setCurrentBet(player.getCurrentBet());
            playersGameStatusDto[i].setLastAction(player.getLastAction().toString());
            playersGameStatusDto[i].setPlayerTitle(player.getPlayerTitle().toString());
            playersGameStatusDto[i].setTokens(player.getTokens());
            playersGameStatusDto[i].setPlayerCards(player.getPlayerCards());
            playersGameStatusDto[i].setActive(gameManager.isAnActivePlayer(player.getPlayerId()));
            i++;
        }

        return playersGameStatusDto;
    }

    public TableInfoDto getTableInfo(){
        PokerTableView tableInfo = gameManager.getTableView();
        TableInfoDto tableDto = new TableInfoDto();

        tableDto.setPot(tableInfo.getPot());
        tableDto.setCards(tableInfo.getTableCards());
        return tableDto;
    }

    public List<WinnerInfoDto> getWinners() throws InvalidOperationException{
        if(winners == null)
            throw new InvalidOperationException("Error- getting winners method is invalid in this part of the game");

        List<WinnerInfoDto> winnerInfoDtos = new ArrayList<>(winners.size());
        for(WinnerInfo winner: winners){
            WinnerInfoDto winnerInfoDto = new WinnerInfoDto();
            winnerInfoDto.setHandRank(winner.getHandRank());
            winnerInfoDto.setPlayerName(winner.getName());
            winnerInfoDto.setPlayerType(winner.getPlayerType());
            winnerInfoDto.setTotalBuys(winner.getTotalBuys());
            winnerInfoDto.setTotalHandsPlayed(winner.getTotalHandsPlayed());
            winnerInfoDto.setTotalWinsNumber(winner.getTotalWinsNumber());
            winnerInfoDto.setWinningPrice(winner.getWinningPrice());

            winnerInfoDtos.add(winnerInfoDto);
        }
        return winnerInfoDtos;
    }

    public boolean isGameRoomFull(){

        return roomMaxCapacity == roomUserManager.getUsersCount();
    }

    public String getGameEndReason() {
        return gameEndReason;
    }

    public String getHandEndReason() {
        return handEndReason;
    }

    public void startGame() throws InvalidOperationException{
        ActionResult result = registerPlayers();
        if(!result.isSucceed())
            throw new InvalidOperationException(result.getMsgError());

        result = gameManager.startNewGame();
        if(!result.isSucceed())
            throw new InvalidOperationException(result.getMsgError());

        gameStatus = RoomStatuses.Active;
    }

    public void startHand() {
        winners = null;
        handEndReason = "";

        ActionResult canStart = gameManager.canStartNewHand();
        if (!canStart.isSucceed()) {
            System.out.println("ERROR- Can't start new hand " + canStart.getMsgError());
            return;
        }

        if(!isThereAnyHumanActive()) {
            System.out.println("Cant start hand-there is no human player with enough tokens to enter the hand");
            endEntireGame("There is no human player with enough tokens to play. game is finished");
            return;
        }

        ActionResult result = gameManager.startNewHandRound();
        if (!result.isSucceed()) {
            System.out.println("Error- Can't start hand round " + result.getMsgError());
            return;
        }
        isFirstHandStarted = true;
        gameManager.startNewGambleRound();
    }

    public boolean isHandRoundEnded(){
        if(!isFirstHandStarted)
            return true;
        return gameManager.isHandRoundEnded();
    }

    public boolean isHandRoundStarted() {
        return gameManager.isHandRoundStarted();
    }

    public PokerActionValidationDto getValidActionForPlayer(String playerName){
        HashMap<PokerAction,Boolean> validPokerActions = gameManager.getValidPokerActionsForPlayer(gameManager.findPlayerIdByName(playerName));
        PokerActionValidationDto pokerActionValidationDto = new PokerActionValidationDto();
        for(PokerAction action: validPokerActions.keySet()){
            switch (action){
                case RAISE: {
                    pokerActionValidationDto.setRaiseValid(validPokerActions.get(action));
                    break;
                }
                case FOLD: {
                    pokerActionValidationDto.setFoldValid(validPokerActions.get(action));
                    break;
                }
                case CALL: {
                    pokerActionValidationDto.setCallValid(validPokerActions.get(action));
                    break;
                }
                case BET: {
                    pokerActionValidationDto.setBetValid(validPokerActions.get(action));
                    break;
                }
                case CHECK: {
                    pokerActionValidationDto.setCheckValid(validPokerActions.get(action));
                    break;
                }
            }
        }
        return pokerActionValidationDto;
    }

    public ActionResult MakePokerAction(String playerName, int action, int value){
        int playerId = gameManager.findPlayerIdByName(playerName);
        PokerAction pokerAction = PokerAction.parseFromInt(action);
        ActionResult result;
        switch (pokerAction){
            case BET:{
                result = gameManager.Bet(playerId,value);
                break;
            }
            case CHECK:{
                result = gameManager.Check(playerId);
                break;
            }
            case CALL:{
                result = gameManager.Call(playerId);
                break;
            }
            case FOLD:{
                result = gameManager.Fold(playerId);
                break;
            }
            case RAISE:{
                result = gameManager.Raise(playerId,value);
                break;
            }
            case COMPUTER:{
                PlayerSignInDto playerInfo = roomUserManager.getUser(playerName);
                if(!playerInfo.isComputer())
                    return new ActionResult(false,"Error- only computer players can ask for poker action-computer!");

                result = handleComputerPlayerTurn(playerName);
                break;
            }
            default:{
                result = new ActionResult(false,"Error- invalid action type");
                break;
            }
        }
        if(!result.isSucceed())
            return result;

        handlePlayerEndTurn();
        return result;


    }

    public SimpleResultDto isMyTurn(String playerName){
        return new SimpleResultDto(gameManager.isPlayerTurnValidation(playerName));
    }

    public ActionResult buyTokens(String playerName){

        return gameManager.buyTokens(playerName);
    }


    private ActionResult registerPlayers(){
        PlayerSignInDto[] signInInfos = roomUserManager.getAllUsersOrdered();
        ArrayList<PlayerRegistration> playerRegistrations = new ArrayList<>();
        int id = 1;
        for(PlayerSignInDto signInInfo: signInInfos){
            PlayerTypes playerType;
            if(signInInfo.isComputer())
                playerType = PlayerTypes.Computer;
            else
                playerType = PlayerTypes.Human;

            PlayerRegistration registrationInfo = new PlayerRegistration(id,playerType,signInInfo.getPlayerName());
            playerRegistrations.add(registrationInfo);
            id++;
        }

        return gameManager.registerPlayers(playerRegistrations,false);
    }

    private ActionResult handleComputerPlayerTurn(String playerName){
        ActionResult actionResult;
        IExternalPlayer playerInterface = new ComputerPlayer();
        PlayerInfo currPlayerInfo = gameManager.getPlayerInfoById(gameManager.findPlayerIdByName(playerName));
        PokerTableView pokerTableView = gameManager.getTableView();
        PokerAction playerAction = playerInterface.GetNextAction(currPlayerInfo, pokerTableView);
        actionResult = gameManager.isPokerActionValid(currPlayerInfo.getPlayerId(),playerAction);
        if(!actionResult.isSucceed())
            return actionResult;
        if(actionResult.isSucceed()) {
            actionResult = executeComputerPlayerAction(playerInterface,currPlayerInfo,pokerTableView,playerAction);
        }
        return actionResult;
    }

    private void handlePlayerEndTurn() {

        if (areAllHumanPlayersFold()) {
            killHand("All human players folded");
            return;
        }

        if (gameManager.isGambleRoundDone()) {
            if (!gameManager.isAllActivePlayerHasTokens()) {
                gameManager.skipAllGambleRounds();
                System.out.println("Player doesn't have enough tokens- Press the ok button to finish the hand and see the winners");
                finishHand("There is a player which doesn't have enough tokens to continue");
                return;
            }
            gameManager.startNewGambleRound();

            if(gameManager.isHandRoundEnded()){
                finishHand("all gamble rounds ended");
                return;
            }
        }
    }

    private boolean areAllHumanPlayersFold(){
        try {
            PlayerInfo[] handPlayers = gameManager.getActivePlayersStatus();
            for(PlayerInfo info:handPlayers){
                if(info.getPlayerType() == PlayerTypes.Human && !gameManager.isPlayerFold(info.getPlayerId()))
                    return false;
            }
        }catch (InvalidOperationException ex){
            System.out.println("Invalid function call- you are not allowed to call to getActivePlayersStatus at the moment");
            return false;
        }
        return true;
    }

    private void killHand(String endReason){
        System.out.println("Hand was forcibly finish- All human players have folded");
        winners = gameManager.killHand(-1);
        handEndReason = endReason;

        //todo: save the winners!
        handleEndOfHand();
    }

    private void finishHand(String endReason){
        try {
             winners = gameManager.finishHandAndGetWinners();
             handEndReason = endReason;

            //todo: save the winners
            handleEndOfHand();
        }catch (InvalidOperationException ex){
                System.out.println("Invalid function call- you are not allowed to call to finishHandAndGetWinners at the moment");
        }
    }

    private void handleEndOfHand() {
        roomUserManager.InitAllReadyStatuses();

        if (gameManager.isAllHandsDone()) {
            System.out.println("All hands have ended. Game is finished");
            endEntireGame("All hands have ended. Game is finished");
            return;
        }

        if (!isThereAnyHumanActiveInTheEntireGame()) {
            System.out.println("There is no more human players in the room. Game is finished");
            endEntireGame("There is no more human players in the room. Game is finished");
            return;
        }
    }

    private void endEntireGame(String endReason){
        gameStatus = RoomStatuses.End;
        gameEndReason = endReason;
    }

    private boolean isThereAnyHumanActiveInTheEntireGame(){
        try {
            PlayerInfo[] Players = gameManager.getPlayersStatus();
            for(PlayerInfo info:Players){
                if(info.getPlayerType() == PlayerTypes.Human) {
                    if(!info.isPlayerFoldFromEntireGame())
                        return true;
                }
            }
        }catch (InvalidOperationException ex){
            System.out.println("Failed to check 'isThereAnyHumanActiveInTheEntireGame'.you are not allowed to call to getActivePlayersStatus at the moment");
        }
        return false;
    }

    private boolean isThereAnyHumanActive(){
        try {
            PlayerInfo[] Players = gameManager.getPlayersStatus();
            for(PlayerInfo info:Players){
                if(info.getPlayerType() == PlayerTypes.Human) {
                    if(gameManager.isAnActivePlayer(info.getPlayerId()))
                        return true;
                }
            }
        }catch (InvalidOperationException ex){
            System.out.println("Invalid function call 'isThereAnyHumanActive'-you are not allowed to call to getActivePlayersStatus at the moment");
        }
        return false;
    }

    private ActionResult executeComputerPlayerAction(IExternalPlayer playerInterface, PlayerInfo playerInfo, PokerTableView tableView, PokerAction playerAction){
        switch (playerAction) {
            case FOLD: {
                playerInterface.Fold(playerInfo,tableView);
                return gameManager.Fold(playerInfo.getPlayerId());
            }
            case CALL: {
                playerInterface.Call(playerInfo,tableView);
                return gameManager.Call(playerInfo.getPlayerId());
            }
            case CHECK: {
                playerInterface.Check(playerInfo,tableView);
                return gameManager.Check(playerInfo.getPlayerId());
            }
            case BET: {
                int betValue = playerInterface.Bet(playerInfo,tableView);
                return gameManager.Bet(playerInfo.getPlayerId(),betValue);
            }
            case RAISE: {
                int raiseValue = playerInterface.Raise(playerInfo,tableView);
                return gameManager.Raise(playerInfo.getPlayerId(),raiseValue);
            }
            default: {
                return new ActionResult(false,"Invalid poker action");
            }
        }
    }
}
