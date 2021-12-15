package agents.alan;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;

public class AStarer extends AbsActioner{
    private MarioAgent AStarAgent;
    private boolean initAgent = true;

    public AStarer() {
        this.AStarAgent = new agents.robinBaumgarten.Agent();
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        if(initAgent == true) { //DON'T INITIALIZE THE ASTAR EVERY FRAME
            this.AStarAgent.initialize(model, timer);
            initAgent = false;
        }
        return AStarAgent.getActions(model, timer);
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
