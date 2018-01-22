package StateMachine.Processors;

import StateMachine.MachineStates.GameStates;

public class GameStateProcessor extends MachineProcessor<GameStates> {

    public GameStateProcessor() {
        initializeTransitions();
        this.currStateIndex = 0;
    }

    @Override
    public void initializeTransitions() {

        this.statesTransition = new GameStates[]{GameStates.None, GameStates.SettingsLoaded,
                GameStates.PlayerRegisterd,
                GameStates.GameStarted,
                GameStates.GameEnded};
    }

    @Override
    public boolean isSubStateCanBeExecute(GameStates subState) {
        switch (subState){
            case GetGameStatus:{
                if(statesTransition[currStateIndex] != GameStates.None)
                    return true;
                return false;
            }
            default: return false;
        }
    }
}
