package ServletContexts;

import Business.GameManager;
import Business.GameSettings;
import Common.ActionResult;
import Common.GlobalDefines.RoomStatuses;
import Common.PlayerUtilities.GameGeneralInfo;
import Common.PlayerUtilities.PlayerInfo;
import Common.gameExceptions.InvalidOperationException;
import PokerDtos.*;

public class GameRoom {

    private UsersManager roomUserManager;
    private GameManager gameManager;
    private RoomStatuses gameStatus;
    private String roomOwner;

    public GameRoom(String roomOwner){
        this.roomOwner = roomOwner;
        gameStatus = RoomStatuses.Pending;
        roomUserManager = new UsersManager();
        gameManager = new GameManager();
    }

    public boolean joinRoom(PlayerSignInDto playerDetails){
        return roomUserManager.addUser(playerDetails);
    }

    public void leaveRoom(String playerName){
        roomUserManager.removeUser(playerName);
    }

    public synchronized ActionResult loadRoomSettings(String settings){
        ActionResult result = gameManager.loadGameSettingsFromString(settings);

        return result;
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

    public PlayerInfoDto[] getPlayersInfo() throws InvalidOperationException{
        PlayerInfo[] playersInfo = gameManager.getPlayersStatus();
        PlayerInfoDto[] playersInfoDto = new PlayerInfoDto[playersInfo.length];

        int i = 0;
        for(PlayerInfo player : playersInfo){
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
            playersGameStatusDto[i].setPlayerName(player.getPlayerName());
            playersGameStatusDto[i].setCurrentBet(player.getCurrentBet());
            playersGameStatusDto[i].setLastAction(player.getLastAction().toString());
            playersGameStatusDto[i].setPlayerTitle(player.getPlayerTitle().toString());
            playersGameStatusDto[i].setTokens(player.getTokens());
            playersGameStatusDto[i].setPlayerCards(player.getPlayerCards());
            i++;
        }

        return playersGameStatusDto;
    }



}
