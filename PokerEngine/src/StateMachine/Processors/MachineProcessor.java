package StateMachine.Processors;

public abstract class MachineProcessor<T> {

    protected T [] statesTransition;
    protected int currStateIndex;

    public abstract void initializeTransitions();

    public boolean isCurrentSateEquals(T state){
        return statesTransition[currStateIndex] == state;
    }

    public void moveToNextState(){
        if(this.currStateIndex < statesTransition.length - 1)
            this.currStateIndex ++;
    }

    public boolean isNextStateValid(T nextState){
        if(currStateIndex == statesTransition.length - 1)
            return false;

        return statesTransition[currStateIndex + 1] == nextState;
    }

    public void ForceNextState(T nextState){
        for(int i = 0; i < statesTransition.length; i++){
            if(statesTransition[i] == nextState) {
                this.currStateIndex = i;
                break;
            }
        }
    }

    public boolean isCurrStateBiggerThen(T state) {
        int stateIndex = 0;
        for (int i = 0; i < statesTransition.length; i++) {
            if (statesTransition[i] == state) {
                stateIndex = i;
                break;
            }

        }
        if(stateIndex < currStateIndex )
            return true;
        return false;
    }

    public boolean isSubStateCanBeExecute(T subState){return false;}

    public  boolean isStateMachineDone(){
        return currStateIndex == statesTransition.length -1;
    }
}
