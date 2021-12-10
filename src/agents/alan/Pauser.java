package agents.alan;

import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

public class Pauser extends AbsActioner{

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        stateRepetitions++;
        boolean[] result = new boolean[MarioActions.numberOfActions()];
        result[MarioActions.PAUSE.getValue()] = true;
        return result;
    }

    @Override
    public STATE getNextState(MarioForwardModel model, MarioTimer timer) {
        if(stateRepetitions > RNG.nextInt(100) + 100){
            return STATE.DEFAULT;
        } else return STATE.PAUSE;
    }
}
