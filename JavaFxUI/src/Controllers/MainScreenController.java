package Controllers;

import Business.GameManager;
import Business.LoadSettingsTask;
import Common.ActionResult;
import Common.ExternalPlayer.ComputerPlayer;
import Common.ExternalPlayer.IExternalPlayer;
import Common.GlobalDefines.GameDefines;
import Common.GlobalDefines.PlayerTypes;
import Common.GlobalDefines.PokerAction;
import Common.PlayerUtilities.GameGeneralInfo;
import Common.PlayerUtilities.PlayerInfo;
import Common.PlayerUtilities.PokerTableView;
import Common.PlayerUtilities.WinnerInfo;
import Common.gameExceptions.InvalidOperationException;
import Utilities.CardsImageManager;
import Utilities.PlayerFoldFromGameNotification;
import Utilities.ReplayIterator;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainScreenController implements PlayerFoldFromGameNotification{

    private Stage primaryStage;
    private GameManager pokerGameEngine;
    private boolean isSettingsLoaded;
    private boolean isReplayModeOn;
    private ReplayIterator handReplay;
    private Map<Integer,PokerPlayerViewController> playersViews;
    private CardsImageManager cardsImageManager;

    @FXML VBox TableLayout;
    @FXML BorderPane GameMainLayout;

    @FXML Button loadSettingsButton;
    @FXML Button startGameButton;
    @FXML Button startHandButton;

    @FXML Button prevReplayButton;
    @FXML Button nextReplayButton;
    @FXML Button replayButton;

    @FXML Label handsCountLabel;
    @FXML Label buyValueLabel;
    @FXML Label bigValueLabel;
    @FXML Label smallValueLabel;
    @FXML Label blindFixedLabel;
    @FXML Label additionsLabel;
    @FXML Label totalBuysLabel;

    @FXML Label LoadingStatusLabel;
    @FXML ProgressBar LoadingStatusProgress;

    @FXML TableView<PlayerInfo> registeredPlayersTableView;
    @FXML TableColumn playerIdColumn;
    @FXML TableColumn playerNameColumn;
    @FXML TableColumn playerTypeColumn;
    @FXML TableColumn playerBuysColumn;
    @FXML TableColumn playerWinsColumn;
    @FXML Button showCardsButton;
    @FXML Button betButton;
    @FXML Button callButton;
    @FXML Button raiseButton;
    @FXML Button foldButton;
    @FXML Button checkButton;
    @FXML Button buyTokensButton;
    @FXML TextField betInput;
    @FXML TextField raiseInput;


    @FXML Label potLabel;
    @FXML ImageView tableCardImage1;
    @FXML ImageView tableCardImage2;
    @FXML ImageView tableCardImage3;
    @FXML ImageView tableCardImage4;
    @FXML ImageView tableCardImage5;

    @FXML HBox playersBottomLayout;
    @FXML HBox playersTopLayout;
    @FXML VBox playersLeftLayout;
    @FXML VBox playersRightLayout;

    private ImageView [] tableCardsImages;
    private Pane[] playersLayouts;

    private SimpleStringProperty loadStatusProperty;

    private SimpleStringProperty handsCountProperty;
    private SimpleIntegerProperty buyValueProperty;
    private SimpleIntegerProperty bigValueProperty;
    private SimpleIntegerProperty smallValueProperty;
    private SimpleStringProperty blindFixedProperty;
    private SimpleIntegerProperty additionsProperty;

    private SimpleBooleanProperty loadSettingsButtonDisable;
    private SimpleBooleanProperty startGameButtonDisable;
    private SimpleBooleanProperty startHandButtonDisable;

    private SimpleBooleanProperty showCardsButtonDisable;
    private SimpleBooleanProperty betButtonDisable;
    private SimpleBooleanProperty callButtonDisable;
    private SimpleBooleanProperty raiseButtonDisable;
    private SimpleBooleanProperty foldButtonDisable;
    private SimpleBooleanProperty checkButtonDisable;
    private SimpleBooleanProperty betInputDisable;
    private SimpleBooleanProperty raiseInputDisable;
    private SimpleBooleanProperty buyTokensButtonDisable;

    private SimpleBooleanProperty replayButtonDisable;
    private SimpleBooleanProperty prevReplayButtonDisable;
    private SimpleBooleanProperty nextReplayButtonDisable;

    private SimpleIntegerProperty potLabelProperty;
    private SimpleIntegerProperty totalBuysLabelProperty;

    public MainScreenController(){
        tableCardsImages = new ImageView[GameDefines.MAX_TABLE_CARDS];
        playersLayouts = new Pane[4];
        playersViews = new HashMap<>();
        isReplayModeOn = false;
        cardsImageManager = new CardsImageManager();

        isSettingsLoaded = false;
        loadStatusProperty = new SimpleStringProperty();
        handsCountProperty = new SimpleStringProperty();
        buyValueProperty = new SimpleIntegerProperty();
        bigValueProperty = new SimpleIntegerProperty();
        smallValueProperty = new SimpleIntegerProperty();
        blindFixedProperty = new SimpleStringProperty();
        additionsProperty = new SimpleIntegerProperty();
        potLabelProperty = new SimpleIntegerProperty();
        totalBuysLabelProperty = new SimpleIntegerProperty();
        loadSettingsButtonDisable = new SimpleBooleanProperty(false);
        startGameButtonDisable = new SimpleBooleanProperty(true);
        startHandButtonDisable = new SimpleBooleanProperty(true);
        showCardsButtonDisable = new SimpleBooleanProperty(true);
        betButtonDisable = new SimpleBooleanProperty(true);
        callButtonDisable = new SimpleBooleanProperty(true);
        raiseButtonDisable = new SimpleBooleanProperty(true);
        foldButtonDisable = new SimpleBooleanProperty(true);
        checkButtonDisable = new SimpleBooleanProperty(true);
        betInputDisable = new SimpleBooleanProperty(true);
        raiseInputDisable = new SimpleBooleanProperty(true);
        buyTokensButtonDisable = new SimpleBooleanProperty(true);
        prevReplayButtonDisable = new SimpleBooleanProperty(true);
        nextReplayButtonDisable = new SimpleBooleanProperty(true);
        replayButtonDisable = new SimpleBooleanProperty(true);
    }

    @FXML
    private void initialize() {
        bindGameMenuActionsProperties();
        bindPokerActionProperties();
        bindReplayActionProperties();
        bindRegisteredPlayersProperties();
        BindTableProperties();
        initializeTableCardsImagesView();
        initializePlayersLayout();
        updateLoadingViewVisability(false);
        TableLayout.setId("TableLayout");
        GameMainLayout.setId("GameMainLayout");
    }

    @FXML
    private void loadGameSettings() throws InvalidOperationException,Exception{
        String settingsFilePath = showSettingsFileDialog();
        startGameButtonDisable.set(true);
        if(settingsFilePath == null)
            return;

        LoadSettingsTask loadSettingsTask = pokerGameEngine.getAsyncSettingLoaderObject();
        bindLoadingStatusView(loadSettingsTask);
        try {
            LoadingStatusLabel.setTextFill(Color.BLACK);
            updateLoadingViewVisability(true);
            loadSettingsButtonDisable.set(true);
            loadSettingsTask.valueProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    onLoadSettingsDone(newValue);
                } catch (InvalidOperationException ex) {
                    loadSettingsButtonDisable.set(false);
                }
            });

            pokerGameEngine.loadGameSettingsAsync(loadSettingsTask, settingsFilePath);

        }catch (InvalidOperationException ex) {
            showMsgBox("Error","Error in settings file loading",ex.getMessage());
            loadSettingsButtonDisable.set(false);
        }
    }

    private void onLoadSettingsDone(ActionResult result) throws InvalidOperationException{
        if(!result.isSucceed()){
            LoadingStatusLabel.setTextFill(Color.RED);
            showMsgBox("Error","Error in settings file",result.getMsgError());
            loadSettingsButtonDisable.set(false);
            return;
        }
        startGameButtonDisable.set(false);
        LoadingStatusLabel.setTextFill(Color.GREEN);
        if(!isSettingsLoaded)
            bindGameGeneralInfoProperties();
        updateGameGeneralInfo();
        updateRegisteredPlayersTableView();
        fillPlayersLayouts();
        updateTotalBuysLabel();

        startGameButtonDisable.set(false);
        loadSettingsButtonDisable.set(false);
    }

    private void showWinningDialog(List<WinnerInfo> winners,PlayerInfo[] activePlayers){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ViewComponents/WinningScreen.fxml"));
            Parent root1 = fxmlLoader.load();
            WinningScreenController winningScreenController = fxmlLoader.getController();
            Stage stage = new Stage();
            winningScreenController.setPrimaryStage(stage);
            winningScreenController.organizeWinners(winners,activePlayers);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Winning board");
            stage.setScene(new Scene(root1));
            stage.showAndWait();
        }catch (Exception ex){

        }
    }

    @FXML
    private void startGame() throws InvalidOperationException{
        updateLoadingViewVisability(false);
        loadSettingsButtonDisable.set(true);
        startHandButtonDisable.set(false);
        startGameButtonDisable.set(true);
        buyTokensButtonDisable.set(false);
        ActionResult actionResult = pokerGameEngine.startNewGame();
        if(!actionResult.isSucceed()){
            showMsgBox("Error","Can't start new game",actionResult.getMsgError());
            return;
        }
        PlayerInfo[] playersInfo = pokerGameEngine.getPlayersStatus();
        for(PlayerInfo player:playersInfo){
            if(!player.isPlayerFoldFromEntireGame() && player.getPlayerType() == PlayerTypes.Human)
                playersViews.get(player.getPlayerId()).setFoldGameButtonVisabillity(true);
        }
    }

    @FXML
    private void startHand(){

        if(pokerGameEngine.isAllHandsDone()){
            showMsgBox("Info","Can't start new hand","All hands are done. Please load a new game");
            pokerGameEngine.forceInitializeNewGame();
            prepareNewGameState();
            return;
        }

        ActionResult canStart = pokerGameEngine.canStartNewHand();
        if(!canStart.isSucceed())
        {
            showMsgBox("Error","Can't start new hand",canStart.getMsgError());
            return;
        }
        if(!isThereAnyHumanActiveInTheEntireGame())
        {
            showMsgBox("Error","Cant start hand","All human players folded from the game. Please load a new game");
            pokerGameEngine.forceInitializeNewGame();
            prepareNewGameState();
            return;
        }
        if(!isThereAnyHumanActive()){
            showMsgBox("Error","Cant start hand","there is no human player with enough tokens to enter the hand");
            return;
        }

        ActionResult result = pokerGameEngine.startNewHandRound();
        if (!result.isSucceed()) {
            showMsgBox("Error","Can't start hand round",result.getMsgError());
            return;
        }
        try {
            pokerGameEngine.startNewGambleRound();
            updateViewOnHandStart();
            updateGameGeneralInfo();
            updatePokerTablePotLabel(null);
            updatePokerTableCardsView(null);
            updateAllPokerPlayersViews(true);
            handleAllUnactivePlayersViews();
            handleNextPlayerTurn();
        }catch (InvalidOperationException ex){
            showMsgBox("Fatal error","Unexpected error",ex.getMessage());
        }

    }

    @FXML
    private void prevReplay() {
        boolean updateAllInPrevActions = false;
        if(handReplay.isNewRound())
            updateAllInPrevActions = true;
        if (handReplay.backward()) {
            if(updateAllInPrevActions)
            {
                PlayerInfo[] lastActions = handReplay.getAllPlayersLastAction();
                for(PlayerInfo lastAction: lastActions){
                    updatePlayerViewsForReplay(lastAction,false);
                }
            }
            updatePrevPlayerOnReplayBackwards();
            updateOnNewReplayState();
        }
        else
            prevReplayButtonDisable.set(true);
        if(nextReplayButtonDisable.get() == true)
            nextReplayButtonDisable.set(false);
    }

    @FXML
    private void nextReplay() {
        if (handReplay.forward())
            updateOnNewReplayState();
        else
            nextReplayButtonDisable.set(true);
        if(prevReplayButtonDisable.get() == true)
            prevReplayButtonDisable.set(false);
    }

    private void updateOnNewReplayState(){
        PokerTableView tableView = handReplay.getCurrTableView();
        updatePokerTableCardsView(tableView);
        if(handReplay.isNewRound()){
            for (PlayerInfo playerInfo : handReplay.getActivePlayers())
                updatePlayerViewsForReplay(playerInfo,false);
        }
        updatePokerTablePotLabel(handReplay.getCuurPot());
        updatePlayerViewsForReplay(handReplay.getCurrPlayerActionResult(),true);
    }
    private void updatePrevPlayerOnReplayBackwards(){
        PlayerInfo lastPlayerAction= handReplay.getLastPlayerAction();
        if(lastPlayerAction != null)
            updatePlayerViewsForReplay(lastPlayerAction,false);
    }

    @FXML
    private void replay(){
        if(isReplayModeOn){
            isReplayModeOn = false;
            replayButton.setText("start replay");
            nextReplayButtonDisable.set(true);
            prevReplayButtonDisable.set(true);
            buyTokensButtonDisable.set(false);
            startHandButtonDisable.set(false);
            deselectAllPlayersViews();
            updateAllPokerPlayersViews(false);
            enableAllPlayersView();
        }
        else {
            handReplay = new ReplayIterator(pokerGameEngine.getGameReplay());
            for (PlayerInfo playerInfo : handReplay.getActivePlayers())
                updatePlayerViewsForReplay(playerInfo,false);
            for(PlayerInfo playerInfo : handReplay.getDisablePlayers())
                playersViews.get(playerInfo.getPlayerId()).disableViewForUnactivePlayer();

            updatePokerTableCardsView(handReplay.getCurrTableView());
            updatePokerTablePotLabel(handReplay.getCurrTableView().getPot());
            isReplayModeOn = true;
            replayButton.setText("end replay");
            nextReplayButtonDisable.set(false);
            prevReplayButtonDisable.set(false);
            buyTokensButtonDisable.set(true);
            startHandButtonDisable.set(true);
        }
    }

    @FXML
    private void bet() {
        ActionResult result;
        int betValue;
        PlayerInfo currPlayer = pokerGameEngine.getCurrentPlayerInfo();
        try {
            betValue = Integer.parseInt(betInput.textProperty().get());
        }catch (Exception ex){
            showMsgBox("Error", "Invalid bet value!", "value must be a number");
            return;
        }

        result = pokerGameEngine.Bet(currPlayer.getPlayerId(),betValue);
        if (!result.isSucceed()) {
            showMsgBox("Error", "Invalid poker action!", result.getMsgError());
            return;
        }
        betInput.clear();
        handlePlayerEndTurn(pokerGameEngine.getPlayerInfoById(currPlayer.getPlayerId()));
    }
    @FXML
    private void check(){
        ActionResult result;
        PlayerInfo currPlayer = pokerGameEngine.getCurrentPlayerInfo();
        result = pokerGameEngine.Check(currPlayer.getPlayerId());
        if(!result.isSucceed()) {
            showMsgBox("Error", "Invalid poker action!", result.getMsgError());
            return;
        }
        handlePlayerEndTurn(pokerGameEngine.getPlayerInfoById(currPlayer.getPlayerId()));
    }
    @FXML
    private void call() {
        ActionResult result;
        PlayerInfo currPlayer = pokerGameEngine.getCurrentPlayerInfo();
        result = pokerGameEngine.Call(currPlayer.getPlayerId());
        if (!result.isSucceed()) {
            showMsgBox("Error", "Invalid poker action!", result.getMsgError());
            return;
        }
        handlePlayerEndTurn(pokerGameEngine.getPlayerInfoById(currPlayer.getPlayerId()));
    }
    @FXML
    private void raise(){
        ActionResult result;
        int raiseValue;
        PlayerInfo currPlayer = pokerGameEngine.getCurrentPlayerInfo();
        try {
            raiseValue = Integer.parseInt(raiseInput.textProperty().get());
        }catch (Exception ex){
            showMsgBox("Error", "Invalid raise value!", "value must be a number");
            return;
        }

        result = pokerGameEngine.Raise(currPlayer.getPlayerId(),raiseValue);
        if (!result.isSucceed()) {
            showMsgBox("Error", "Invalid poker action!", result.getMsgError());
            return;
        }
        raiseInput.clear();
        handlePlayerEndTurn(pokerGameEngine.getPlayerInfoById(currPlayer.getPlayerId()));
    }
    @FXML
    private void fold() {
        ActionResult result;
        PlayerInfo currPlayer = pokerGameEngine.getCurrentPlayerInfo();
        result = pokerGameEngine.Fold(currPlayer.getPlayerId());
        if (!result.isSucceed()) {
            showMsgBox("Error", "Invalid poker action!", result.getMsgError());
            return;
        }
        disablePlayerView(currPlayer.getPlayerId());
        handlePlayerEndTurn(pokerGameEngine.getPlayerInfoById(currPlayer.getPlayerId()));
    }

    @FXML
    private void buyTokens() throws InvalidOperationException {
        PlayerInfo selectedPlayer = registeredPlayersTableView.getSelectionModel().getSelectedItem();
        selectedPlayer = pokerGameEngine.getPlayerInfoById(selectedPlayer.getPlayerId()); // get updated status of player...
        if (selectedPlayer == null) {
            showMsgBox("Error", "Can't buy tokens", "please select player from the players table");
            return;
        }
        if (selectedPlayer.getPlayerType() == PlayerTypes.Computer) {
            showMsgBox("Error", "Can't buy tokens", "computer player can't buy tokens");
            registeredPlayersTableView.getSelectionModel().clearSelection();
            return;
        }
        if(selectedPlayer.isPlayerFoldFromEntireGame())
        {
            showMsgBox("Error", "Can't buy tokens", "The player has folded from the entire game and therefore can't buy tokens");
            registeredPlayersTableView.getSelectionModel().clearSelection();
            return;
        }
        ActionResult result = pokerGameEngine.buyTokens(selectedPlayer.getPlayerId());
        if (!result.isSucceed())
            showMsgBox("Error", "Buying operation failed", result.getMsgError());
        else
            updateTotalBuysLabel();

        registeredPlayersTableView.getSelectionModel().clearSelection();
        updateRegisteredPlayersTableView();
        updateAllPokerPlayersViews(false);
    }

    @FXML
    private void showPlayerCards(){
        PlayerInfo currPlayer = pokerGameEngine.getCurrentPlayerInfo();
        PokerPlayerViewController playerViewController = playersViews.get(currPlayer.getPlayerId());
        playerViewController.showCards(currPlayer.getPlayerCards());
    }

    @FXML
    private void hidePlayerCards(){
        PlayerInfo currPlayer = pokerGameEngine.getCurrentPlayerInfo();
        PokerPlayerViewController playerViewController = playersViews.get(currPlayer.getPlayerId());
        playerViewController.hideCards();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setPokerGameEngine(GameManager pokerGameEngine){
        this.pokerGameEngine = pokerGameEngine;
    }

    private void showMsgBox(String title,String header, String context)
    {
        Alert alertBox = new Alert(Alert.AlertType.INFORMATION);
        alertBox.setTitle(title);
        alertBox.setHeaderText(header);
        alertBox.setContentText(context);
        alertBox.showAndWait();
    }

    private String showSettingsFileDialog(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select game settings file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xml files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) {
            return null;
        }

        String absolutePath = selectedFile.getAbsolutePath();
        return absolutePath;
    }

    private boolean areAllHumanPlayersFold(){
        try {
            PlayerInfo[] handPlayers = pokerGameEngine.getActivePlayersStatus();
            for(PlayerInfo info:handPlayers){
                if(info.getPlayerType() == PlayerTypes.Human && !pokerGameEngine.isPlayerFold(info.getPlayerId()))
                    return false;
            }
        }catch (InvalidOperationException ex){
            showMsgBox("Error","Invalid function call","you are not allowed to call to getActivePlayersStatus at the moment");
        }
        return true;
    }

    private boolean isThereAnyHumanActiveInTheEntireGame(){
        try {
            PlayerInfo[] Players = pokerGameEngine.getPlayersStatus();
            for(PlayerInfo info:Players){
                if(info.getPlayerType() == PlayerTypes.Human) {
                    if(!info.isPlayerFoldFromEntireGame())
                        return true;
                }
            }
        }catch (InvalidOperationException ex){
            showMsgBox("Error","Invalid function call","you are not allowed to call to getActivePlayersStatus at the moment");
        }
        return false;
    }

    private boolean isThereAnyHumanActive(){
        try {
            PlayerInfo[] Players = pokerGameEngine.getPlayersStatus();
            for(PlayerInfo info:Players){
                if(info.getPlayerType() == PlayerTypes.Human) {
                    if(pokerGameEngine.isAnActivePlayer(info.getPlayerId()))
                        return true;
                }
            }
        }catch (InvalidOperationException ex){
            showMsgBox("Error","Invalid function call","you are not allowed to call to getActivePlayersStatus at the moment");
        }
        return false;
    }

    private int getRandomHumanPlayerId(){
        try {
            PlayerInfo[] handPlayers = pokerGameEngine.getActivePlayersStatus();
            for(PlayerInfo info:handPlayers){
                if(info.getPlayerType() == PlayerTypes.Human)
                    return info.getPlayerId();
            }
        }catch (InvalidOperationException ex){
            showMsgBox("Error","Invalid function call","you are not allowed to call to getActivePlayersStatus at the moment");
        }
        return -1;
    }

    private void finishHand(){
        try {
            PlayerInfo[] activePlayers = pokerGameEngine.getActivePlayersStatus();
            List<WinnerInfo> winners = pokerGameEngine.finishHandAndGetWinners();
            showWinningDialog(winners,activePlayers);
            updateViewOnHandEnd();
        }catch (InvalidOperationException ex){
            showMsgBox("Error","Invalid function call","you are not allowed to call to finishHandAndGetWinners at the moment");
        }
    }

    private void killHand(){
        showMsgBox("Notice","Hand was forcibly finish","All human players have folded");
        try {
            PlayerInfo[] activePlayers = pokerGameEngine.getActivePlayersStatus();
            List<WinnerInfo> winners = pokerGameEngine.killHand(getRandomHumanPlayerId());
            showWinningDialog(winners, activePlayers);
            updateViewOnHandEnd();
        }catch (InvalidOperationException ex){
            showMsgBox("Error","can't finish hand",ex.getMessage());
        }
    }

    private void handlePlayerEndTurn(PlayerInfo currPlayer) {
        playersViews.get(currPlayer.getPlayerId()).updatePlayerInfo(currPlayer,false);
        playersViews.get(currPlayer.getPlayerId()).isSelectedPlayer(false);
        showCardsButtonDisable.set(true);

        updatePokerTablePotLabel(null);

        if (areAllHumanPlayersFold()) {
            killHand();
            return;
        }

        if (pokerGameEngine.isGambleRoundDone()) {
            if (!pokerGameEngine.isAllActivePlayerHasTokens()) {
                pokerGameEngine.skipAllGambleRounds();
                showMsgBox("Info","Player doesn't have enough tokens","Press the ok button to finish the hand and see the winners");
                updatePokerTableCardsView(null);
                finishHand();
                return;
            }
            pokerGameEngine.startNewGambleRound();
            updatePlayerViewsForNextRound();

            if(pokerGameEngine.isHandRoundEnded()){
                finishHand();
                return;
            }
            updatePokerTableCardsView(null);
        }

        handleNextPlayerTurn();
    }

    private void handleNextPlayerTurn(){
        PlayerInfo currPlayerInfo = pokerGameEngine.getCurrentPlayerInfo();
        PokerTableView pokerTableView = pokerGameEngine.getTableView();
        playersViews.get(currPlayerInfo.getPlayerId()).isSelectedPlayer(true);
        if(currPlayerInfo.getPlayerType() == PlayerTypes.Computer) {
            handleComputerPlayerTurn(currPlayerInfo, pokerTableView);
        }
        else
            handleHumanPlayerTurn(currPlayerInfo);
    }

    private void handleComputerPlayerTurn(PlayerInfo currPlayer, PokerTableView pokerTableView){
        ActionResult actionResult;
        IExternalPlayer playerInterface = new ComputerPlayer();
        PokerAction playerAction = playerInterface.GetNextAction(currPlayer, pokerTableView);
        actionResult = pokerGameEngine.isPokerActionValid(currPlayer.getPlayerId(),playerAction);
        if(actionResult.isSucceed()) {
            actionResult = executeComputerPlayerAction(playerInterface,currPlayer,pokerTableView,playerAction);
            if(playerAction == PokerAction.FOLD)
                disablePlayerView(currPlayer.getPlayerId());
            handlePlayerEndTurn(pokerGameEngine.getPlayerInfoById(currPlayer.getPlayerId()));
        }
        else{
            showMsgBox("Error","Error in computer player action",actionResult.getMsgError());
        }
    }

    private ActionResult executeComputerPlayerAction(IExternalPlayer playerInterface, PlayerInfo playerInfo, PokerTableView tableView, PokerAction playerAction){
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
    private void handleHumanPlayerTurn(PlayerInfo currPlayer) {
        showCardsButtonDisable.set(false);
        Map<PokerAction, Boolean> actionValidation = pokerGameEngine.getValidPokerActionsForPlayer(currPlayer.getPlayerId());
        for (PokerAction pokerAction : actionValidation.keySet()) {
            switch (pokerAction) {
                case FOLD: {
                    foldButtonDisable.set(!actionValidation.get(pokerAction));
                    break;
                }
                case CALL: {
                    callButtonDisable.set(!actionValidation.get(pokerAction));
                    break;
                }
                case CHECK: {
                     checkButtonDisable.set(!actionValidation.get(pokerAction));
                    break;
                }
                case BET: {
                    betButtonDisable.set(!actionValidation.get(pokerAction));
                    betInputDisable.set(!actionValidation.get(pokerAction));
                    break;
                }
                case RAISE: {
                    raiseButtonDisable.set(!actionValidation.get(pokerAction));
                    raiseInputDisable.set(!actionValidation.get(pokerAction));
                    break;
                }
            }
        }
    }

    private void disablePlayerView(int playerId){
        playersViews.get(playerId).disableViewOnFold();
    }

    private void enableAllPlayersView(){
        for(PokerPlayerViewController playerController : playersViews.values()){
            playerController.enableView();
        }
    }

    private void bindLoadingStatusView(LoadSettingsTask loadSettingsTask){
        LoadingStatusLabel.textProperty().bind(loadSettingsTask.messageProperty());
        LoadingStatusProgress.progressProperty().bind(loadSettingsTask.progressProperty());
    }

    private void bindGameGeneralInfoProperties(){
        handsCountLabel.textProperty().bind(handsCountProperty);
        bigValueLabel.textProperty().bind(Bindings.format("%,d",bigValueProperty));
        smallValueLabel.textProperty().bind(Bindings.format("%,d",smallValueProperty));
        buyValueLabel.textProperty().bind(Bindings.format("%,d",buyValueProperty));
        blindFixedLabel.textProperty().bind(blindFixedProperty);
        additionsLabel.textProperty().bind(Bindings.format("%,d",additionsProperty));
    }

    private void bindGameMenuActionsProperties(){
        loadSettingsButton.disableProperty().bind(loadSettingsButtonDisable);
        startHandButton.disableProperty().bind(startHandButtonDisable);
        startGameButton.disableProperty().bind(startGameButtonDisable);
    }

    private void bindReplayActionProperties(){
        replayButton.disableProperty().bind(replayButtonDisable);
        nextReplayButton.disableProperty().bind(nextReplayButtonDisable);
        prevReplayButton.disableProperty().bind(prevReplayButtonDisable);
    }

    private void bindPokerActionProperties(){
        showCardsButton.disableProperty().bind(showCardsButtonDisable);
        betButton.disableProperty().bind(betButtonDisable);
        raiseButton.disableProperty().bind(raiseButtonDisable);
        callButton.disableProperty().bind(callButtonDisable);
        foldButton.disableProperty().bind(foldButtonDisable);
        checkButton.disableProperty().bind(checkButtonDisable);
        raiseInput.disableProperty().bind(raiseInputDisable);
        betInput.disableProperty().bind(betInputDisable);
        buyTokensButton.disableProperty().bind(buyTokensButtonDisable);
    }

    private void bindRegisteredPlayersProperties(){
        playerIdColumn.setCellValueFactory(new PropertyValueFactory<PlayerInfo,Integer>("playerId"));
        playerNameColumn.setCellValueFactory(new PropertyValueFactory<PlayerInfo,String>("playerName"));
        playerTypeColumn.setCellValueFactory(new PropertyValueFactory<PlayerInfo,String>("playerType"));
        playerBuysColumn.setCellValueFactory(new PropertyValueFactory<PlayerInfo,Integer>("totalBuys"));
        playerWinsColumn.setCellValueFactory(new PropertyValueFactory<PlayerInfo,String>("totalWinsStatistics"));
    }

    private void initializeTableCardsImagesView(){
        tableCardsImages[0] = tableCardImage1;
        tableCardsImages[1] = tableCardImage2;
        tableCardsImages[2] = tableCardImage3;
        tableCardsImages[3] = tableCardImage4;
        tableCardsImages[4] = tableCardImage5;
    }

    private void initializePlayersLayout(){
        playersLayouts[0] = playersRightLayout;
        playersLayouts[1] = playersBottomLayout;
        playersLayouts[2] = playersLeftLayout;
        playersLayouts[3] = playersTopLayout;
    }

    private void BindTableProperties(){
        potLabel.textProperty().bind(Bindings.format("%,d",potLabelProperty));
        totalBuysLabel.textProperty().bind(Bindings.format("%,d",totalBuysLabelProperty));
    }

    private void updateGameGeneralInfo() throws InvalidOperationException{
        GameGeneralInfo generalInfo = pokerGameEngine.getGameGeneralInfo();
        bigValueProperty.set(generalInfo.getBigBlindValue());
        smallValueProperty.set(generalInfo.getSmallBlindValue());
        additionsProperty.set(generalInfo.getBlindAddition());
        buyValueProperty.set(generalInfo.getBuyValue());
        if(generalInfo.isBlindFixed())
            blindFixedProperty.set("True");
        else
            blindFixedProperty.set("False");
        handsCountProperty.set(generalInfo.getTotalPlayedHands() + "/" +generalInfo.getHandsCount());
    }

    private void updateRegisteredPlayersTableView(){
        PlayerInfo [] playersInfo;
        try {
            playersInfo = pokerGameEngine.getPlayersStatus();
        }
        catch (InvalidOperationException ex){
            showMsgBox("Error","Cant update registered players view",ex.getMessage());
            return;
        }
        sortPlayersByPot(playersInfo);
        ObservableList<PlayerInfo> registeredPlayersTableData = FXCollections.observableArrayList(playersInfo);
        registeredPlayersTableView.setItems(registeredPlayersTableData);
    }

    private void sortPlayersByPot(PlayerInfo[] players){
        for (int i=0;i<players.length - 1;i++){
            for(int j=0;j<players.length-1-i;j++){
                if(players[j].getTokens() < players[j+1].getTokens()){
                    PlayerInfo temp = players[j+1];
                    players[j+1] = players[j];
                    players[j] = temp;
                }
            }
        }
    }

    private void fillPlayersLayouts(){
        for (Pane playerLayout:playersLayouts)
            playerLayout.getChildren().clear();
        playersViews.clear();
        PlayerInfo [] playersInfo;
        try {
            playersInfo = pokerGameEngine.getPlayersStatus();
            int i = 0;
            for(PlayerInfo player:playersInfo){
                createPokerPlayerView(player,i/2);
                i++;
            }
        }
        catch (InvalidOperationException ex){
            showMsgBox("Error","Cant update registered players view",ex.getMessage());
            return;
        }
    }

    private void createPokerPlayerView(PlayerInfo player,int layoutIndex) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/ViewComponents/PokerPlayerView.fxml"));
            Node dynamicPlayerView = loader.load();

            PokerPlayerViewController pokerPlayerViewController = loader.getController();
            pokerPlayerViewController.updatePlayerInfo(player,false);
            pokerPlayerViewController.setPlayerId(player.getPlayerId());
            pokerPlayerViewController.setOnFoldEvent(this);

            if(layoutIndex != 2 && layoutIndex != 1)
                playersLayouts[layoutIndex].getChildren().add(dynamicPlayerView);
            else
                playersLayouts[layoutIndex].getChildren().add(0,dynamicPlayerView);
            playersViews.put(player.getPlayerId(), pokerPlayerViewController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateLoadingViewVisability(boolean isVisible){
        LoadingStatusLabel.visibleProperty().setValue(isVisible);
        LoadingStatusProgress.visibleProperty().setValue(isVisible);
    }

    private void updateAllPokerPlayersViews(boolean isHandRunning){
        try {
            PlayerInfo[] playersInfo = pokerGameEngine.getPlayersStatus();
            for(PlayerInfo playerInfo:playersInfo){
                playersViews.get(playerInfo.getPlayerId()).updatePlayerInfo(playerInfo,true);
                if(!isHandRunning){
                    if(playerInfo.getPlayerType()==PlayerTypes.Human && !playerInfo.isPlayerFoldFromEntireGame())
                        playersViews.get(playerInfo.getPlayerId()).setFoldGameButtonVisabillity(true);
                    else
                        playersViews.get(playerInfo.getPlayerId()).setFoldGameButtonVisabillity(false);
                }
                else
                    playersViews.get(playerInfo.getPlayerId()).setFoldGameButtonVisabillity(false);
            }
        }catch (InvalidOperationException ex){

        }
    }

    private void updatePlayerViewsForNextRound(){
        try {
            PlayerInfo[] activePlayers = pokerGameEngine.getActivePlayersStatus();
            for(PlayerInfo playerInfo:activePlayers){
                if(playerInfo.getLastAction() != PokerAction.FOLD)
                    playersViews.get(playerInfo.getPlayerId()).updatePlayerInfo(playerInfo,false);
            }
        }catch (InvalidOperationException ex){

        }
    }

    private void updatePlayerViewsForReplay(PlayerInfo playerInfo, boolean markSelectedPlayer){
        if(playerInfo == null)
            return;
        PokerPlayerViewController playerController = playersViews.get(playerInfo.getPlayerId());
        deselectAllPlayersViews();
        playerController.setFoldGameButtonVisabillity(false);
        if(markSelectedPlayer)
            playerController.isSelectedPlayer(true);
        playerController.updatePlayerInfo(playerInfo,false);
        if(playerInfo.getLastAction() == PokerAction.FOLD)
            playerController.disableViewOnFold();
        else
            playerController.showCards(playerInfo.getPlayerCards());
    }

    private void handleAllUnactivePlayersViews(){
        try {
            PlayerInfo[] activePlayers = pokerGameEngine.getActivePlayersStatus();
            for (int playerId : playersViews.keySet()) {
                boolean found = false;
                for (PlayerInfo activePlayer : activePlayers) {
                    if (activePlayer.getPlayerId() == playerId) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    playersViews.get(playerId).disableViewForUnactivePlayer();
                //if(!pokerGameEngine.isAnActivePlayer(playerId))
                //  playersViews.get(playerId).disableViewForUnactivePlayer();
            }
        } catch (InvalidOperationException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void deselectAllPlayersViews(){
        for(int playerId: playersViews.keySet()){
            playersViews.get(playerId).isSelectedPlayer(false);
        }
    }

    private void updateTotalBuysLabel(){
        try {
            totalBuysLabelProperty.set(pokerGameEngine.getGameStatistics().getTotalTokens());
        }catch (InvalidOperationException ex){

        }
    }

    private void updatePokerTablePotLabel(Integer pot){
        if(pot == null) {
            PokerTableView tableView = pokerGameEngine.getTableView();
            potLabelProperty.set(tableView.getPot());
        }
        else
            potLabelProperty.set(pot);
    }


    private void updatePokerTableCardsView(PokerTableView tableInfo){
        PokerTableView tableView;
        if(tableInfo == null)
             tableView = pokerGameEngine.getTableView();
        else
            tableView = tableInfo;

        String[] cards = tableView.getTableCards();
        int i = 0;
        for(;i < cards.length; i++ ){
                tableCardsImages[i].setImage(cardsImageManager.GetCardImage(cards[i].toUpperCase()));

        }
        for(;i<GameDefines.MAX_TABLE_CARDS;i++) {
            tableCardsImages[i].setImage(cardsImageManager.GetCardImage(null));
        }
    }


    private void updateViewOnHandStart(){
        startHandButtonDisable.set(true);
        replayButtonDisable.set(true);
        nextReplayButtonDisable.set(true);
        prevReplayButtonDisable.set(true);
        buyTokensButtonDisable.set(true);
    }

    private void prepareNewGameState(){
        playersViews.clear();
        for(Pane playerLayout:playersLayouts)
            playerLayout.getChildren().clear();
        for(ImageView cardImage:tableCardsImages)
            cardImage.setImage(null);
        updatePokerTablePotLabel(0);
        loadSettingsButtonDisable.set(false);
        startGameButtonDisable.set(true);
        startHandButtonDisable.set(true);
        showCardsButtonDisable.set(true);
        betButtonDisable.set(true);
        callButtonDisable.set(true);
        raiseButtonDisable.set(true);
        foldButtonDisable.set(true);
        checkButtonDisable.set(true);
        betInputDisable.set(true);
        raiseInputDisable.set(true);
        buyTokensButtonDisable.set(true);
        prevReplayButtonDisable.set(true);
        nextReplayButtonDisable.set(true);
        replayButtonDisable.set(true);
    }

    private void updateViewOnHandEnd(){
        try {
            updateAllPokerPlayersViews(false);
            updateRegisteredPlayersTableView();
            updateGameGeneralInfo();
            enableAllPlayersView();
        }catch (InvalidOperationException ex){
            showMsgBox("Error","Cant update general info view",ex.getMessage());
        }
        startHandButtonDisable.set(false);
        replayButtonDisable.set(false);
        buyTokensButtonDisable.set(false);
        showCardsButtonDisable.set(true);
        betButtonDisable.set(true);
        betInputDisable.set(true);
        callButtonDisable.set(true);
        checkButtonDisable.set(true);
        raiseButtonDisable.set(true);
        raiseInputDisable.set(true);
        foldButtonDisable.set(true);
    }

    @Override
    public void onPlayerFoldFromGame(int playerId) {
        ActionResult result = pokerGameEngine.foldFromEntireGame(playerId);
        if(!result.isSucceed()) {
            showMsgBox("Error", "Action is not allowed", result.getMsgError());
            return;
        }
        playersViews.get(playerId).setFoldGameButtonVisabillity(false);
    }
}
