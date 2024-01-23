package com.sfdev.assembly.transition;

import com.sfdev.assembly.callbacks.CallbackBase;

public class TransitionData {
    private TransitionCondition transitionCondition;
    private String pointerState;
    private CallbackBase exitAction;
    private TransitionCondition minimumTransition;

    /**
     * A class that holds 3 objects.
     * @param transitionCondition The transition condition.
     * @param pointerState The pointer state.
     * @param exitAction The exit action on transition.
     */
    public TransitionData(TransitionCondition transitionCondition, String pointerState, CallbackBase exitAction) {
        this.transitionCondition = transitionCondition;
        this.pointerState = pointerState;
        this.exitAction = exitAction;
        minimumTransition = null;
    }

    /**
     * Determines whether this transition should transition.
     * @return Return whether the state should transition.
     */
    public boolean shouldTransition() {
        if(minimumTransition != null) return transitionCondition.shouldTransition() && minimumTransition.shouldTransition();
        return transitionCondition.shouldTransition();
    }

    /**
     * Starts the timer of the transition condition and/or the minimum transition.
     */
    public void runTimer() {
        if(transitionCondition instanceof TransitionTimed && !((TransitionTimed) transitionCondition).timerStarted()) { // starting all timedTransitions
            ((TransitionTimed) transitionCondition).startTimer();
        }

        if(minimumTransition != null && minimumTransition instanceof TransitionTimed && !((TransitionTimed) minimumTransition).timerStarted()) { // starting all timedTransitions
            ((TransitionTimed) minimumTransition).startTimer();
        }
    }

    /**
     * Restarts the timer at the end of the state.
     */
    public void resetTimer() {
        if(transitionCondition instanceof TransitionTimed && ((TransitionTimed) transitionCondition).timerStarted()) { // starting all timedTransitions
            ((TransitionTimed) transitionCondition).resetTimer();
        }

        if(minimumTransition != null && minimumTransition instanceof TransitionTimed && ((TransitionTimed) minimumTransition).timerStarted()) { // starting all timedTransitions
            ((TransitionTimed) minimumTransition).resetTimer();
        }
    }

    /**
     * Getter for the transition condition.
     * @return Returns the first object that was passed into the constructor.
     */
    protected TransitionCondition getTransitionCondition() {
        return transitionCondition;
    }

    /**
     * Getter for the pointer state.
     * @return Returns the second object that was passed into the constructor.
     */
    public String getPointerState() {
        return pointerState;
    }

    /**
     * Getter for the exit action.
     * @return Returns the third object that was passed into the constructor.
     */
    public CallbackBase getExitAction() {
        return exitAction;
    }

    /**
     * Getter for the minimum transition/
     * @return Returns the minimum transition that will cause the state to transition.
     */
    protected TransitionCondition getMinimumTransition() {
        return minimumTransition;
    }

    /**
     * Setter for the minimum transition.
     * @param minimumTransition Sets the
     */
    public void setMinimumTransition(TransitionCondition minimumTransition) {
        this.minimumTransition = minimumTransition;
    }
}
