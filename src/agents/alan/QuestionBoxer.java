package agents.alan;

import engine.core.MarioForwardModel;
import engine.core.MarioTimer;

public class QuestionBoxer extends AbsActioner{
    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        return new boolean[0];
    }

    @Override
    public STATE getNextState(MarioForwardModel model, MarioTimer timer) {
        return null;
    }
}
