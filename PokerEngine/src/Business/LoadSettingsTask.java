package Business;

import Common.ActionResult;
import Common.GlobalDefines.GameTypes;
import StateMachine.Processors.GameStateProcessor;
import StateMachine.Processors.MachineProcessor;
import Utilities.XmlLoader;
import generatedJaxb.GameDescriptor;
import javafx.concurrent.Task;

import javax.xml.bind.JAXBException;

public class LoadSettingsTask extends Task<ActionResult> {

    private GameManager gameManager;
    private String settingsFilePath;
    private MachineProcessor gameStateProcessor;

    public LoadSettingsTask(GameManager gameManager, MachineProcessor gameStateProcessor){
        this.gameManager = gameManager;
        this.gameStateProcessor = gameStateProcessor;
    }

    @Override
    protected ActionResult call() throws Exception {
        try {
            updateMessage("loading file...");
            GameDescriptor settingsDescriptor = XmlLoader.LoadGameSettings(settingsFilePath);
            updateProgress(30,100);
            Thread.sleep(1100);
            updateMessage("Collecting metadata..");
            GameSettings gameSettings = convertDescriptorToSettings(settingsDescriptor);
            updateProgress(50,100);
            Thread.sleep(1100);
            updateMessage("Validating file...");
            ActionResult result = gameSettings.isGeneralSettingsValid();
            updateProgress(80,100);
            Thread.sleep(1100);

            if(result.isSucceed()){
                gameManager.setGameSettings(gameSettings);
                gameStateProcessor.moveToNextState();
                if(gameSettings.getGameType() == GameTypes.MultiPlayer)
                    result = gameManager.registerPlayers(gameSettings.getFixedPlayersRegistration(),true);
            }

            if(result.isSucceed()) {
                updateProgress(100,100);
                updateMessage("Successfully loaded the file");
            }
            else {
                updateProgress(0,100);
                updateMessage("File loading failed!");
            }

            return result;
        }
        catch (JAXBException e)
        {
            updateMessage("File loading failed!");
            return new ActionResult(false,"!!! Error has occurred during the settings loading. please check the file !!!");

        }
        catch (Exception e){
            updateMessage("File loading failed!");
            return new ActionResult(false,"Error has occurred during the settings loading. seems like some fields are missing in the file!");
        }

    }

    public void setSettingsFilePath(String settingsFilePath) {
        this.settingsFilePath = settingsFilePath;
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
        gameSettings.setDefinedPlayers(descriptor.getPlayers().getPlayer());

        return gameSettings;
    }
}
