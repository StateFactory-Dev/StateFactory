package com.sfdev.assembly.state;

/**
 * A class that holds 3 objects.
 * @param <F> The transition condition.
 * @param <S> The pointer state.
 * @param <T> The exit action on transition.
 */
public class Triple<F, S, T>{

    F first;
    S second;
    T third;

    /**
     * A class that holds 3 objects.
     * @param transitionCondition The transition condition.
     * @param pointerState The pointer state.
     * @param exitAction The exit action on transition.
     */
    public Triple(F transitionCondition, S pointerState, T exitAction) {
        this.first = transitionCondition;
        this.second = pointerState;
        this.third = exitAction;
    }

    /**
     * Getter for the transition condition.
     * @return Returns the first object that was passed into the constructor.
     */
    public F getTransitionCondition() {
        return first;
    }

    /**
     * Getter for the pointer state.
     * @return Returns the second object that was passed into the constructor.
     */
    public S getPointerState() {
        return second;
    }

    /**
     * Getter for the exit action.
     * @return Returns the third object that was passed into the constructor.
     */
    public T getExitAction() {
        return third;
    }
}
