package agents.alan;

import engine.helper.MarioActions;

import java.util.Random;

public class Fidgeter {
    // FIDGET STATE CODE (MIGHT MOVE TO NEW FILE LATER)
    private enum FIDGET_MODE {
        JUMPING,
        PACING,
        SQUATTING,
        ERROR
    }

    private final Random RNG = new Random();
    private FIDGET_MODE mode;
    private int paceDistance; // How far (in frames) Mario will pace before turning around
    private int fidgetRepetitions; // Accumulator for how long since fidget has been changed

    public Fidgeter() {
        this.mode = randomMode();
        this.setPaceDistance();
        this.fidgetRepetitions = 0;
    }

    public boolean[] getFidgetAction(boolean isOnGround) {
        boolean[] result = new boolean[MarioActions.numberOfActions()];
        switch(mode){
            case JUMPING:
                result[MarioActions.JUMP.getValue()] = true;
                if(!isOnGround) return result; //Idle while in air without counting it as a fidget repetition
                break;

            case PACING:
                boolean left = this.fidgetRepetitions < this.paceDistance / 2;
                result[MarioActions.LEFT.getValue()] = left;
                result[MarioActions.RIGHT.getValue()] = !left;
                break;

            case SQUATTING:
                result[MarioActions.DOWN.getValue()] = this.fidgetRepetitions%2 == 0;
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

    private void setPaceDistance(){
        this.paceDistance = RNG.nextInt(20) + 30;
    }

    private boolean updateFidgetState(){
        // This uses a bit of a markov chain to emulate how a real player would fidget
        // There are two ways to fidget: jumping straight up, or pacing left and right
        // Alan will usually do the same thing again, but he may switch to the other fidget mode
        boolean needsToChange = false;
        boolean hasChanged = false;
        switch (this.mode){
            case JUMPING: {
                needsToChange = RNG.nextFloat() < 0.05;
                break;
            }
            case PACING: {
                needsToChange = this.fidgetRepetitions > this.paceDistance;
                break;
            }
            case SQUATTING: {
                needsToChange = RNG.nextFloat() < 0.1;
                break;
            }
            default: {
                needsToChange = true;
            }
        }

        if(needsToChange) {
            FIDGET_MODE oldMode = this.mode;
            this.mode = randomMode();
            hasChanged = this.mode != oldMode;
            //If we stop pacing, then we change the distance of the next pace
            if(oldMode == FIDGET_MODE.PACING && hasChanged) this.setPaceDistance();
        }

        return hasChanged; //Returns true if we've changed
    }
}
