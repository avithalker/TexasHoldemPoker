import Business.GameManager;
import Business.GameStatistics;
import Common.ActionResult;
import Common.ExternalPlayer.ComputerPlayer;
import Common.ExternalPlayer.IExternalPlayer;
import Common.GlobalDefines.PlayerTypes;
import Common.GlobalDefines.PokerAction;
import Common.PlayerUtilities.PlayerInfo;
import Common.PlayerUtilities.PlayerRegistration;
import Common.PlayerUtilities.PokerTableView;
import Common.PlayerUtilities.WinnerInfo;
import Common.gameExceptions.InvalidOperationException;
import ConsoleGlobalDefines.ConsoleGlobals;
import ConsoleMenus.GeneralMessages;
import ConsoleMenus.IMenu;
import ConsoleMenus.MainMenu;
import ConsoleMenus.MenuChoices.MainMenuChoices;

import java.util.*;
import java.util.function.Supplier;


public class ConsoleGameManager {

    private GameManager pokerGameEngine;
    private IMenu<MainMenuChoices> mainMenu;
    private Map<MainMenuChoices,Supplier<ActionResult>> mainMenuChoiceHandler;
    private Map<PlayerTypes,IExternalPlayer> playerInterfaces;
    private boolean isGameEnded;
    private int humanPlayerId = 0;

    public ConsoleGameManager(){
        pokerGameEngine = new GameManager();
        isGameEnded = false;
        mainMenu = new MainMenu();
        initMainMenuChoiceHandler();
        initPlayerInterfaces();
    }

    public void playGame()
    {
        MainMenuChoices mainMenuChoice;
        ActionResult actionResult;

        while (!isGameEnded) {
            do {
                mainMenu.printMenu();
                mainMenuChoice = mainMenu.getMenuChoice();
                Supplier<ActionResult> choiceHandler = mainMenuChoiceHandler.get(mainMenuChoice);
                actionResult = choiceHandler.get();
                if(!actionResult.isSucceed()) {
                    System.out.println();
                    System.out.println(actionResult.getMsgError());
                }
            }while (!actionResult.isSucceed());
            if(!pokerGameEngine.isHasMoreHands())
                exitGame();
        }
    }

    private void initMainMenuChoiceHandler() {
        mainMenuChoiceHandler = new HashMap<>();
        mainMenuChoiceHandler.put(MainMenuChoices.LoadSettings, () -> loadSettings());
        mainMenuChoiceHandler.put(MainMenuChoices.StartGame, () -> startGame());
        mainMenuChoiceHandler.put(MainMenuChoices.ShowGameStatus, () -> showGameStatus());
        mainMenuChoiceHandler.put(MainMenuChoices.PlayHand, () -> playHand());
        mainMenuChoiceHandler.put(MainMenuChoices.GetStatistics, () -> getStatistics());
        mainMenuChoiceHandler.put(MainMenuChoices.Buy, () -> buyTokens());
        mainMenuChoiceHandler.put(MainMenuChoices.Exit, () -> exitGame());
    }

    private void initPlayerInterfaces(){
        playerInterfaces = new HashMap<>();
        playerInterfaces.put(PlayerTypes.Computer, new ComputerPlayer());
        playerInterfaces.put(PlayerTypes.Human,new HumanPlayer());
    }

    private ActionResult loadSettings()
    {
        ActionResult result;
        Scanner scanner = ConsoleGlobals.getInputScanner();
        System.out.println("Please enter the path of the settings file:");
        String path = scanner.nextLine();
        result = pokerGameEngine.loadGameSettings(path);
        if(!result.isSucceed())
            return result;

        result = registerPlayers();
        if(!result.isSucceed())
            return result;

        System.out.println("\n*** Settings were successfully loaded ***");
        showGameStatus();
        return result;
    }

    private ActionResult startGame(){
        ActionResult result = pokerGameEngine.startNewGame();
        if(result.isSucceed()){
            showGameStatus();
        }
        return result;
    }

    private ActionResult showGameStatus(){
        try {
            PlayerInfo [] playersStatus = pokerGameEngine.getPlayersStatus();
        ConsoleGamePainter.PaintPlayersStatus(playersStatus, pokerGameEngine.getSmallBlindValue(),pokerGameEngine.getBigBlindValue());
        }
        catch (InvalidOperationException e){
            return new ActionResult(false,e.getMessage());
        }

        return new ActionResult(true,"");
    }

    private ActionResult playHand() {
        ActionResult canStart = pokerGameEngine.canStartNewHand();
        if(!canStart.isSucceed())
            return canStart;

        boolean isHandGotKilled = false;
        List<WinnerInfo> winners = null;
        if(!pokerGameEngine.isAnActivePlayer(humanPlayerId))
            return new ActionResult(false,"!!! Error- the human player does'nt have enough tokens to enter the hand !!!");
        ActionResult result = pokerGameEngine.startNewHandRound();
        if (!result.isSucceed())
            return result;
        pokerGameEngine.startNewGambleRound();
        while (!pokerGameEngine.isHandRoundEnded()) {
            PokerTableView tableView = pokerGameEngine.getTableView();
            PlayerInfo currPlayerInfo = pokerGameEngine.getCurrentPlayerInfo();
            try {
                if (currPlayerInfo.getPlayerType() == PlayerTypes.Human)
                    ConsoleGamePainter.PaintGambleRound(pokerGameEngine.getActivePlayersStatus(), pokerGameEngine.getTableView());
            } catch (InvalidOperationException e) {
                return new ActionResult(false, "Error has occurred while trying to receive the players statuses.");
            }
            IExternalPlayer playerInterface = playerInterfaces.get(currPlayerInfo.getPlayerType());
            handlePlayerAction(playerInterface, currPlayerInfo, tableView);
            if(pokerGameEngine.isPlayerFold(humanPlayerId)) {
                winners = pokerGameEngine.killHand(humanPlayerId);
                isHandGotKilled = true;
                break;
            }

            if (pokerGameEngine.isGambleRoundDone()) {
                try {
                    ConsoleGamePainter.PaintGambleRound(pokerGameEngine.getActivePlayersStatus(), pokerGameEngine.getTableView());
                }
                catch (InvalidOperationException e){
                    return new ActionResult(false,"!!! An error occurred during the hand !!!");
                }

                if(!pokerGameEngine.isAllActivePlayerHasTokens()){
                    GeneralMessages.skipAllGambleRoundsConfirmation();
                    pokerGameEngine.skipAllGambleRounds();
                    break;
                }
                GeneralMessages.moveNextGambleRoundConfirmation();
                pokerGameEngine.startNewGambleRound();
            }
        }
            if(isHandGotKilled){
                GeneralMessages.announceOnHandKilled();
                GeneralMessages.announceWinners(winners);
                return result;
            }
        try {
            ConsoleGamePainter.PaintGambleRound(pokerGameEngine.getActivePlayersStatus(), pokerGameEngine.getTableView());
            GeneralMessages.winnerAnnouncementConfirmation();
            winners = pokerGameEngine.finishHandAndGetWinners();
            GeneralMessages.announceWinners(winners);
        } catch (InvalidOperationException e) {
            return new ActionResult(false, "Error- an error has occurred in calculating the winners");
        }
        return result;
    }

    private ActionResult getStatistics(){
        try {
            GameStatistics gameStatistics = pokerGameEngine.getGameStatistics();
            ConsoleGamePainter.paintGameStatistics(gameStatistics, pokerGameEngine.getSmallBlindValue(),pokerGameEngine.getBigBlindValue());
        }
        catch (InvalidOperationException e){
            return new ActionResult(false,e.getMessage());
        }
        return new ActionResult(true,"");
    }

    private ActionResult buyTokens(){
        ActionResult result = pokerGameEngine.buyTokens(humanPlayerId);
        if(result.isSucceed())
            GeneralMessages.announceOnTokenBuy();
        return result;
    }

    private ActionResult exitGame(){
        GameStatistics statistics = null;
        try {
        statistics = pokerGameEngine.getGameStatistics();
        }catch (InvalidOperationException e){

        }
        ConsoleGamePainter.exitGameConfirmation(statistics,pokerGameEngine.getSmallBlindValue(),pokerGameEngine.getBigBlindValue());
        isGameEnded = true;
        return new ActionResult(true,"");
    }

    private ActionResult registerPlayers(){
        ArrayList<PlayerRegistration> registrationInfo = new ArrayList<>();
        humanPlayerId = 1;
        registrationInfo.add(new PlayerRegistration(humanPlayerId, PlayerTypes.Human,""));
        registrationInfo.add(new PlayerRegistration(2, PlayerTypes.Computer,""));
        registrationInfo.add(new PlayerRegistration(3, PlayerTypes.Computer,""));
        registrationInfo.add(new PlayerRegistration(4, PlayerTypes.Computer,""));

        return pokerGameEngine.registerPlayers(registrationInfo,true);
    }

    private void handlePlayerAction(IExternalPlayer playerInterface, PlayerInfo playerInfo, PokerTableView tableView){
        ActionResult actionResult;
        do {
            PokerAction playerAction = playerInterface.GetNextAction(playerInfo, tableView);
            actionResult = pokerGameEngine.isPokerActionValid(playerInfo.getPlayerId(),playerAction);
            if(actionResult.isSucceed()) {
                actionResult = executePlayerAction(playerInterface,playerInfo,tableView,playerAction);
            }

            if(!actionResult.isSucceed()) {
                System.out.println("\n"+actionResult.getMsgError());
                System.out.println("\nPlease choose again: ");
            }
        }while (!actionResult.isSucceed());
    }

    private ActionResult executePlayerAction(IExternalPlayer playerInterface, PlayerInfo playerInfo, PokerTableView tableView, PokerAction playerAction){

        switch (playerAction) {
            case FOLD: {
                playerInterface.Fold(playerInfo,tableView);
                return pokerGameEngine.Fold(playerInfo.getPlayerId());
            }
            case CALL: {
                playerInterface.Call(playerInfo,tableView);
                return pokerGameEngine.Call(playerInfo.getPlayerId());
            }
            case CHECK: {
                playerInterface.Check(playerInfo,tableView);
                return pokerGameEngine.Check(playerInfo.getPlayerId());
            }
            case BET: {
                int betValue = playerInterface.Bet(playerInfo,tableView);
                return pokerGameEngine.Bet(playerInfo.getPlayerId(),betValue);
            }
            case RAISE: {
                int raiseValue = playerInterface.Raise(playerInfo,tableView);
                return pokerGameEngine.Raise(playerInfo.getPlayerId(),raiseValue);
            }
            default: {
                return new ActionResult(false,"Invalid poker action");
            }
        }
    }
}
