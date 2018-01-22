package Controllers;

import Common.PlayerUtilities.PlayerInfo;
import Common.PlayerUtilities.WinnerInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

public class WinningScreenController {

    private Stage primaryStage;

    @FXML Label handRank;
    @FXML Button okButton;
    @FXML TableView<WinnerInfo> winningTableView;
    @FXML TableColumn winnerNameColumn;
    @FXML TableColumn winnerTypeColumn;
    @FXML TableColumn winnerBuysColumn;
    @FXML TableColumn winnerWinningPriceColumn;
    @FXML TableColumn winnerTotalWinsColumn;

    @FXML
    private void initialize() {
        bindWinnersProperties();
    }

    @FXML
    private void closeWindow(){

        primaryStage.close();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void organizeWinners(List<WinnerInfo> winners, PlayerInfo[] activePlayers){
        for (PlayerInfo player: activePlayers){
            boolean isExist = false;
            for(int i=0;i<winners.size();i++){
                if(player.getPlayerId() == winners.get(i).getPlayerId()){
                    isExist = true;
                    break;
                }
            }
            if(!isExist){
                WinnerInfo newWinnerEntity = new WinnerInfo(player.getPlayerId(),"");
                newWinnerEntity.setWinningPrice(0);
                newWinnerEntity.setName(player.getPlayerName());
                newWinnerEntity.setPlayerType(player.getPlayerType());
                newWinnerEntity.setTotalBuys(player.getTotalBuys());
                newWinnerEntity.setTotalHandsPlayed(player.getTotalHandsPlayed());
                newWinnerEntity.setTotalWinsNumber(player.getTotalWins());
                winners.add(newWinnerEntity);
            }
        }
        ObservableList<WinnerInfo> registeredPlayersTableData = FXCollections.observableArrayList(winners);
        winningTableView.setItems(registeredPlayersTableData);
        handRank.textProperty().setValue(winners.get(0).getHandRank());
    }

    private void bindWinnersProperties(){
        winnerBuysColumn.setCellValueFactory(new PropertyValueFactory<PlayerInfo,Integer>("totalBuys"));
        winnerNameColumn.setCellValueFactory(new PropertyValueFactory<PlayerInfo,String>("name"));
        winnerTypeColumn.setCellValueFactory(new PropertyValueFactory<PlayerInfo,String>("playerType"));
        winnerWinningPriceColumn.setCellValueFactory(new PropertyValueFactory<PlayerInfo,Integer>("winningPrice"));
        winnerTotalWinsColumn.setCellValueFactory(new PropertyValueFactory<PlayerInfo,String>("totalWins"));
    }
}
