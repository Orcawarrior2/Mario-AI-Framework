package agents.alan;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

public class AStarer extends AbsActioner{
    private final MarioAgent AStarAgent;
    private boolean initAgent = true;
    private final boolean speedDemon;

    public AStarer() {
        this.AStarAgent = new agents.robinBaumgarten.Agent();
        this.speedDemon = RNG.nextFloat() < 0.2; //20% chance to be a speed demon
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        if(initAgent) { //DON'T INITIALIZE THE ASTAR EVERY FRAME
            this.AStarAgent.initialize(model, timer);
            initAgent = false;
        }
        boolean[] result = AStarAgent.getActions(model, timer);
        result[MarioActions.SPEED.getValue()] = this.speedDemon;
        return result;
    }

    @Override
    public STATE getNextState(MarioForwardModel model, MarioTimer timer) {
        framesWaited++;
        if(framesWaited == frameWaitThresh) {
            framesWaited = 0;
            if (RNG.nextFloat() < 0.4)
                return STATE.FIDGET;
            else
                return STATE.DEFAULT;
        }
        return STATE.DEFAULT;
    }
}
