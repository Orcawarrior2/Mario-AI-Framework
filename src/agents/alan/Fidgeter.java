package agents.alan;

import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

public class Fidgeter extends AbsActioner {
    // FIDGET STATE CODE (MIGHT MOVE TO NEW FILE LATER)
    private enum FIDGET_MODE {
        JUMPING,
        PACING,
        SQUATTING,
        ERROR
    }

    private FIDGET_MODE mode;
    private int paceDistance; // How far (in frames) Mario will pace before turning around
    private int fidgetRepetitions; // Accumulator for how long since fidget has been changed
    private boolean isSpeed = false;

    public Fidgeter() {
        this.mode = randomMode();
        this.randomizePaceDistance();
        this.fidgetRepetitions = 0;
    }

    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        boolean[] result = new boolean[MarioActions.numberOfActions()];
        result[MarioActions.SPEED.getValue()] = this.isSpeed;
        switch(mode){
            case JUMPING:
                if(!model.mayMarioJump()) return result; //Idle while in air without counting it as a fidget repetition
                result[MarioActions.JUMP.getValue()] = true;
                break;

            case PACING:
                boolean left = this.fidgetRepetitions < this.paceDistance / 2;
                result[MarioActions.LEFT.getValue()] = left;
                result[MarioActions.RIGHT.getValue()] = !left;
                break;

            case SQUATTING:
                int period = this.paceDistance/4;
                result[MarioActions.DOWN.getValue()] = this.fidgetRepetitions%period < (period/2);
                break;
        }
        boolean change = updateFidgetState();
        if(change) this.fidgetRepetitions = 0;
        else this.fidgetRepetitions++;
        return result;
    }

    private FIDGET_MODE randomMode(){
        return FIDGET_MODE.values()[RNG.nextInt(FIDGET_MODE.values().length-1)];
    }

    private void randomizePaceDistance(){
        this.paceDistance = RNG.nextInt(20) + 30;
    }

    private boolean updateFidgetState(){
        // This uses a bit of a markov chain to emulate how a real player would fidget
        // There are two ways to fidget: jumping straight up, or pacing left and right
        // Alan will usually do the same thing again, but he may switch to the other fidget mode
        boolean needsToChange;
        boolean hasChanged = false;
        float fidgetRepeatFloat = (float) (1/(this.fidgetRepetitions+1));
        switch (this.mode){
            case JUMPING:
            case SQUATTING: {
                needsToChange = fidgetRepeatFloat < RNG.nextFloat();
                break;
            }
            case PACING: {
                needsToChange = this.fidgetRepetitions > this.paceDistance;
                break;
            }
            default: {
                needsToChange = true;
            }
        }

        if(needsToChange) {
            FIDGET_MODE oldMode = this.mode;
            this.mode = randomMode();
            this.isSpeed = RNG.nextBoolean();
            hasChanged = this.mode != oldMode;
            //If we stop pacing, then we change the distance of the next pace
            if(oldMode == FIDGET_MODE.PACING && hasChanged) this.randomizePaceDistance();
        }
        return hasChanged; //Returns true if we've changed
    }

    @Override
    public STATE getNextState(MarioForwardModel model, MarioTimer timer) {
        framesWaited++;
        if(framesWaited == frameWaitThresh) {
            framesWaited = 0;
            if (RNG.nextFloat() < 0.2) {
                return STATE.PAUSE;
            } else if (RNG.nextFloat() < 0.4) {
                return STATE.FIDGET;
            } else{
                return STATE.DEFAULT;
            }
        }
        return STATE.FIDGET;
    }
}
