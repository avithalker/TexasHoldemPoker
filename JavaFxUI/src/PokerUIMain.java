import Business.GameManager;
import Controllers.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

public class PokerUIMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        URL mainScreenFXML = getClass().getResource("/ViewComponents/MainScreen.fxml");
        loader.setLocation(mainScreenFXML);
        ScrollPane root = loader.load();

        MainScreenController mainScreenController = loader.getController();
        GameManager pokerGaneEngine = new GameManager();
        mainScreenController.setPrimaryStage(primaryStage);
        mainScreenController.setPokerGameEngine(pokerGaneEngine);

        // set stage
        primaryStage.setTitle("Texas Holdem Poker");
        Scene scene = new Scene(root, 1050, 600);
        scene.getStylesheets().add(getClass().getResource("/Resources/MainScreenStyle.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
