package agents.alan;

import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

public class PowerupGrabber extends AbsActioner{
    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        boolean result[] = new boolean[MarioActions.numberOfActions()];

        if(this.framesWaited > this.frameWaitThresh)
            result[MarioActions.RIGHT.getValue()] = true;
        this.framesWaited++;

        return result;
    }

    @Override
    public STATE getNextState(MarioForwardModel model, MarioTimer timer) {
        if(this.framesWaited > (this.frameWaitThresh * 2))
            return STATE.FIDGET;
        else
            return STATE.POWERUP_GRAB;
    }
}
