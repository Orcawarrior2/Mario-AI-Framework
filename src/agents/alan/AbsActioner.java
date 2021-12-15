package agents.alan;

import engine.core.MarioForwardModel;
import engine.core.MarioTimer;

import java.util.Random;

public abstract class AbsActioner {
    enum STATE {
        DEFAULT,        // Normal A* Algorithm from the RobinBaumgaten
        QUESTION_BOX,   // When Mario is within a certain distance to a "? box", bump it
        POWERUP_GRAB,   // Once a powerup is generated from a "? box", this state makes Mario grab it
        ENEMY_SMASH,    // Enemy is detected, Mario stomps it
        FIDGET,         // Instead of sitting still while, Alan bounces around like humans do
        PAUSE,          // Alan gets bored and sometimes pauses to get snacks or something (rarer)
        FIGURING,       // Mario lacks tutorials and Alan is new to the game, so at first he's still learning controls
        // Future ideas:
        // Realizing there's a timer and moving faster but sloppier
        // "Accidentally" start running and just start spamming it like he's practicing
        // Trying to figure out what a gumba does, goes up to pet it
        // Not knowing how to open a ? block

    }

    Random RNG = new Random();
    public int stateRepetitions = 0;
    public int framesWaited = 0;
    public int frameWaitThresh = 30;

    abstract public boolean[] getActions(MarioForwardModel model, MarioTimer timer);
    abstract public STATE getNextState(MarioForwardModel model, MarioTimer timer);
}
