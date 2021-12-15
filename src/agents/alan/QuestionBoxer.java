package agents.alan;

import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

public class QuestionBoxer extends AbsActioner{
    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
         boolean[] result = new boolean[MarioActions.numberOfActions()];


         if(model.getMarioFloatPos()[0] >= 260.0f && model.getMarioFloatPos()[0] <= 263.0f)
             result[MarioActions.JUMP.getValue()] = true;
         else if(model.getMarioFloatPos()[0] < 324.0f)
             result[MarioActions.RIGHT.getValue()] = true;
         else if(model.getMarioFloatPos()[0] > 330.0f)
             result[MarioActions.JUMP.getValue()] = true;
         else
             result[MarioActions.RIGHT.getValue()] = false;

         return result;
    }

    @Override
    public STATE getNextState(MarioForwardModel model, MarioTimer timer) {
        if(model.getMarioFloatPos()[0] < 332.0f)
            return STATE.QUESTION_BOX;
        else
            return STATE.POWERUP_GRAB;
    }
}
