package StateMachine.Processors;

import StateMachine.MachineStates.HandRoundStates;

public class HandRoundStateProcessor extends MachineProcessor<HandRoundStates> {

    public HandRoundStateProcessor() {
        initializeTransitions();
        currStateIndex = 0;
    }

    @Override
    public void initializeTransitions() {
        statesTransition = new HandRoundStates[]{HandRoundStates.None,
                HandRoundStates.GameStarted, HandRoundStates.HandRoundStarted,
                HandRoundStates.HandRoundEnded};

    }
}
