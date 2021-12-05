package agents.alan;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

/**
 * @author Matthew Aguiar and Justin Mitchell
 * CS/IMGD 4100 Group #9
 */
public class Agent implements MarioAgent {

    private enum STATE {
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
        // Rage quitting?
        // Trying to figure out what a gumba does, goes up to pet it
        // Not knowing how to open a ? block
        // Alan gives up and installs hax/mods?

    }

    private STATE currentState;
    private Fidgeter fidgeter;
    private boolean[] nextMove;
    private boolean[] previousMove;
    /**
     * initialize and prepare the agent before the game starts
     *
     * @param model a forward model object so the agent can simulate or initialize some parameters based on it.
     * @param timer amount of time before the agent has to return
     */
    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        this.currentState = STATE.FIDGET;
        this.fidgeter = new Fidgeter();
        this.nextMove = new boolean[MarioActions.numberOfActions()];
        this.previousMove = new boolean[MarioActions.numberOfActions()];
    }

    /**
     * get mario current actions
     *
     * @param model a forward model object so the agent can simulate the future.
     * @param timer amount of time before the agent has to return the actions.
     * @return an array of the state of the buttons on the controller
     */
    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        switch(this.currentState){
            case FIGURING:
                return new boolean[1];
            case FIDGET:
                return this.fidgeter.getFidgetAction();

            case PAUSE:
                return new boolean[2];

            default:
                return new boolean[0];
        }
    }

    /**
     * Return the name of the agent that will be displayed in debug purposes
     *
     * @return String of this agent's name. It's Alan, as in Alan Turing
     */
    @Override
    public String getAgentName() {
        return "Alan";
    }


}
