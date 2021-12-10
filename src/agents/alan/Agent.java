package agents.alan;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

/**
 * @author Matthew Aguiar and Justin Mitchell
 * CS/IMGD 4100 Group #9
 */
public class Agent extends AbsActioner implements MarioAgent {
    private STATE currentState;
    private AbsActioner actioner;
    /**
     * initialize and prepare the agent before the game starts
     *
     * @param model a forward model object so the agent can simulate or initialize some parameters based on it.
     * @param timer amount of time before the agent has to return
     */
    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        this.currentState = STATE.FIDGET;
        this.actioner = getActioner();
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
        boolean[] result = this.actioner.getActions(model, timer);

        STATE nextState = this.getNextState(model, timer);
        if(nextState != this.currentState) {
            this.actioner = getActioner();
        }
        if(this.actioner == null){
            result = new boolean[MarioActions.numberOfActions()];
        }
        return result;
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

    @Override
    public STATE getNextState(MarioForwardModel model, MarioTimer timer) {
        return this.actioner.getNextState(model, timer);
    }

    private AbsActioner getActioner() {
        switch(this.currentState){
            case DEFAULT: return new AStarer();
            case QUESTION_BOX: return new QuestionBoxer();
            case POWERUP_GRAB: return new PowerupGrabber();
            case ENEMY_SMASH: return new EnemySmasher();
            case FIDGET: return new Fidgeter();
            case PAUSE: return new Pauser();
            case FIGURING: return null;
            default: return null;
        }
    }
}
