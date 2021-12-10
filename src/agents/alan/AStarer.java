package agents.alan;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;

public class AStarer extends AbsActioner{
    private MarioAgent AStarAgent;

    public AStarer() {
        this.AStarAgent = new agents.robinBaumgarten.Agent();
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        this.AStarAgent.initialize(model, timer);
        return AStarAgent.getActions(model, timer);
    }

    @Override
    public STATE getNextState(MarioForwardModel model, MarioTimer timer) {
        return STATE.DEFAULT;
    }
}
