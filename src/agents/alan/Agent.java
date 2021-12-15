package agents.alan;

import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;

/**
 * @author Matthew Aguiar and Justin Mitchell
 * CS/IMGD 4100 Group #9
 */
public class Agent implements MarioAgent {
    private AbsActioner.STATE currentState;
    private AbsActioner actioner;

    /**
     * initialize and prepare the agent before the game starts
     *
     * @param model a forward model object so the agent can simulate or initialize some parameters based on it.
     * @param timer amount of time before the agent has to return
     */
    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        this.currentState = AbsActioner.STATE.QUESTION_BOX;
        this.actioner = getNewActioner();

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

        AbsActioner.STATE nextState = this.actioner.getNextState(model, timer);
        if(nextState != this.currentState) {
            // Update Actioner if the state has changed
            this.currentState = nextState;
            this.actioner = getNewActioner();
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

    private AbsActioner getNewActioner() {
        switch(this.currentState){
            case DEFAULT: return new AStarer();
            case QUESTION_BOX: return new QuestionBoxer();
            case POWERUP_GRAB: return new PowerupGrabber();
            case ENEMY_SMASH: return new EnemySmasher();
            case FIDGET: return new Fidgeter();
            case PAUSE: return new Pauser();
            case FIGURING: return new Figurer();
            default: return null;
        }
    }
}
