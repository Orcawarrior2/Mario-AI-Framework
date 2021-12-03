package agents.alan;

import engine.helper.MarioActions;

import java.util.Random;

public class Fidgeter {
    // FIDGET STATE CODE (MIGHT MOVE TO NEW FILE LATER)
    private enum FIDGET_MODE {
        JUMPING,
        PACE_LEFT,
        PACE_RIGHT,
        ERROR
    }

    private Random RNG = new Random();
    private FIDGET_MODE mode;
    private int paceDistance; // How far (in frames) Mario will pace before turning around
    private int fidgetRepetitions; // Accumulator for how long since fidget has been changed

    public Fidgeter() {
        this.mode = randomMode();
        this.paceDistance = RNG.nextInt(10) + 100;
        this.fidgetRepetitions = 0;

    }

    public boolean[] getFidgetAction() {
        boolean[] result = new boolean[MarioActions.numberOfActions()];
        switch(mode){
            case JUMPING -> {
                result[MarioActions.JUMP.getValue()] = true;
            }
            case PACE_LEFT -> {
                result[MarioActions.LEFT.getValue()] = true;
                result[MarioActions.RIGHT.getValue()] = false;
                result[MarioActions.SPEED.getValue()] = false;
            }
            case PACE_RIGHT -> {
                result[MarioActions.LEFT.getValue()] = false;
                result[MarioActions.RIGHT.getValue()] = true;
                result[MarioActions.SPEED.getValue()] = false;
            }
        }
        boolean change = updateFidgetState();
        this.fidgetRepetitions += (change) ? 0 : 1;
        return result;
    }

    private FIDGET_MODE randomMode(){
        return FIDGET_MODE.values()[RNG.nextInt(FIDGET_MODE.values().length-1)];
    }

    private boolean isPacing(FIDGET_MODE mode){
        return mode == FIDGET_MODE.PACE_LEFT || mode == FIDGET_MODE.PACE_RIGHT;
    }

    private boolean updateFidgetState(){
        // This uses a bit of a markov chain to emulate how a real player would fidget
        // There are two ways to fidget: jumping straight up, or pacing left and right
        // Alan will usually do the same thing again, but he may switch to the other fidget mode
        boolean needsToChange = false;
        boolean hasChanged = false;
        if(this.mode == FIDGET_MODE.JUMPING){
            //As we jump more and more, there's an increasing chance that we change
            needsToChange = (RNG.nextFloat() / this.fidgetRepetitions) < 0.05;
        } else if(isPacing(this.mode)){
            if(this.fidgetRepetitions > this.paceDistance && RNG.nextFloat() < 0.9) {
                this.mode = (this.mode == FIDGET_MODE.PACE_LEFT) ? FIDGET_MODE.PACE_RIGHT : FIDGET_MODE.PACE_LEFT;
                hasChanged = true;
                //When we change fidget direction, there's also a 5% chance to change fidget randomly
                if(RNG.nextFloat() < 0.05) needsToChange = true;
            }
        } else if(this.mode == FIDGET_MODE.ERROR) needsToChange = true;

        if(needsToChange) {
            FIDGET_MODE oldMode = this.mode;
            this.mode = randomMode();
            hasChanged = this.mode != oldMode;
            //If we stop pacing, then we change the distance of the next pace
            if(isPacing(oldMode) && !isPacing(this.mode)) paceDistance = RNG.nextInt(100) + 100;
        }

        return hasChanged; //Returns true if we've changed
    }
}
