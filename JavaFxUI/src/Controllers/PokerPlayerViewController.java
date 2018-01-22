package Controllers;

import Common.GlobalDefines.GameDefines;
import Common.GlobalDefines.PokerAction;
import Common.PlayerUtilities.PlayerInfo;
import Utilities.CardsImageManager;
import Utilities.PlayerFoldFromGameNotification;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;


import java.io.File;

public class PokerPlayerViewController {

    @FXML VBox rootLayout;
    @FXML Label nameLabel;
    @FXML Label titleLabel;
    //@FXML Label lastActionLabel;
    @FXML Label betLabel;
    @FXML Label tokensLabel;
    @FXML ImageView playerCardImageView1;
    @FXML ImageView playerCardImageView2;
    @FXML Button foldGameButton;

    Image foldImage;
    Image unActiveImage;
    final String FOLD_IMAGE_PATH="/Resources/fold.png";
    final String NOT_ACTIVE_IMAGE_PATH="/Resources/not active.png";

    private PlayerFoldFromGameNotification onFoldEvent;
    private int playerId;
    private SimpleStringProperty nameProperty;
    private SimpleStringProperty titleProperty;
   // private SimpleStringProperty lastActionProperty;
    private SimpleIntegerProperty betProperty;
    private SimpleIntegerProperty tokensProperty;
    private SimpleBooleanProperty rootLayoutDisable;
    private SimpleBooleanProperty foldGameButtonVisabillity;

    private CardsImageManager cardsImageManager;

    public PokerPlayerViewController(){
        cardsImageManager = new CardsImageManager();

        nameProperty = new SimpleStringProperty();
        titleProperty = new SimpleStringProperty();
      //  lastActionProperty = new SimpleStringProperty();
        betProperty = new SimpleIntegerProperty();
        tokensProperty = new SimpleIntegerProperty();
        rootLayoutDisable = new SimpleBooleanProperty(false);
        foldGameButtonVisabillity = new SimpleBooleanProperty(false);

        try {
            //File foldImageFile = new File(getClass().getResource(FOLD_IMAGE_PATH).toURI().getPath());
            foldImage = new Image(FOLD_IMAGE_PATH);
            //File unActiveImageFile = new File(getClass().getResource(NOT_ACTIVE_IMAGE_PATH).toURI().getPath());
            unActiveImage = new Image(NOT_ACTIVE_IMAGE_PATH);
        }catch (Exception ex){}

    }

    @FXML
    private void initialize(){
        nameLabel.textProperty().bind(nameProperty);
        titleLabel.textProperty().bind(titleProperty);
      //  lastActionLabel.textProperty().bind(lastActionProperty);
        betLabel.textProperty().bind(Bindings.format("%,d",betProperty));
        tokensLabel.textProperty().bind(Bindings.format("%,d",tokensProperty));
        rootLayout.disableProperty().bind(rootLayoutDisable);
        foldGameButton.visibleProperty().bind(foldGameButtonVisabillity);
    }

    @FXML
    private void foldFromGame() {
        if (onFoldEvent != null)
            onFoldEvent.onPlayerFoldFromGame(playerId);
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setOnFoldEvent(PlayerFoldFromGameNotification onFoldEvent) {
        this.onFoldEvent = onFoldEvent;
    }

    public void showCards(String [] cards){
        playerCardImageView1.setImage(cardsImageManager.GetCardImage(cards[GameDefines.FIRST_CARD_INDEX]));
        playerCardImageView2.setImage(cardsImageManager.GetCardImage(cards[GameDefines.SECOND_CARD_INDEX]));
    }

    public void hideCards(){
        playerCardImageView1.setImage(cardsImageManager.GetCardImage(null));
        playerCardImageView2.setImage(cardsImageManager.GetCardImage(null));
    }

    public void disableViewOnFold(){
        playerCardImageView1.setImage(foldImage);
        playerCardImageView2.setImage(foldImage);
        rootLayoutDisable.set(true);
    }

    public void disableViewForUnactivePlayer(){
        playerCardImageView1.setImage(unActiveImage);
        playerCardImageView2.setImage(unActiveImage);
        rootLayoutDisable.set(true);
    }

    public void enableView(){
        rootLayoutDisable.set(false);
    }

    public void isSelectedPlayer(boolean isSelected) {
        if (isSelected) {
            rootLayout.setBorder(new Border(new BorderStroke(Color.RED,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
        else
        {
            rootLayout.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }
    }

    public void setFoldGameButtonVisabillity(boolean isVisible){
        foldGameButtonVisabillity.set(isVisible);
    }

    public void updatePlayerInfo(PlayerInfo playerInfo, boolean ignoreLastAction) {
        nameProperty.set(playerInfo.getPlayerName());
        titleProperty.set(playerInfo.getPlayerTitle().toString());
       // lastActionProperty.set(playerInfo.getLastAction().toString());
        betProperty.set(playerInfo.getCurrentBet());
        tokensProperty.set(playerInfo.getTokens());
        String[] cards = playerInfo.getPlayerCards();
        if(playerInfo.getLastAction() != PokerAction.FOLD || ignoreLastAction ) {
            if (cards.length == 2) {
                playerCardImageView1.setImage(cardsImageManager.GetCardImage(null));
                playerCardImageView2.setImage(cardsImageManager.GetCardImage(null));
            } else {
                playerCardImageView1.setImage(null);
                playerCardImageView2.setImage(null);
            }
        }
    }
}
