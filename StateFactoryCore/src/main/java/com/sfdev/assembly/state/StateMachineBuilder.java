package com.sfdev.assembly.state;

import com.sfdev.assembly.callbacks.CallbackBase;
import com.sfdev.assembly.transition.TransitionCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds each state for the StateMachine.
 */
public class StateMachineBuilder { // takes in the enum of states
    private List<State> stateList = new ArrayList<>();
    private CallbackBase update;

    private boolean useUpdateConstructor = false;

    /**
     *
     * @param stateName Provides an enum constant to represent the state being created.
     */
    public StateMachineBuilder state(Enum stateName) { // initializing the state
        stateList.add(new State(stateName,null, new ArrayList<>(), false));
        return this;
    }

    /**
     * In a fallback state, you MUST point to another state when transitioning & the only way to enter is via a transition pointing to the (fallback) state.
     * @param stateName Provides an enum constant to represent the state being created.
     * @param isFailsafe Indicates to the state machine that the current state is a fallback state. This means it will be ignored when traversing from state to state in a linear order.
     */
    public StateMachineBuilder state(Enum stateName, boolean isFailsafe) { // initializing the state
        stateList.add(new State(stateName, null, new ArrayList<>(), isFailsafe));
        return this;
    }

    /**
     * Example statement:
     *      ".transition( () -> robot.intakeTouchSensor.hasTouched(), Enums.IntakeTransfer)"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the nextState.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     *
     * @param nextState Indicates what the state the StateMachine should transition to after the condition is true.
     */
    public StateMachineBuilder transition(TransitionCondition condition, Enum nextState, CallbackBase exitAction) { // adding the new transition condition & next state
        stateList.get(stateList.size()-1).getTransitions().add(new Triple<>(condition, nextState, exitAction)); // add transition to the last state
        return this;
    }

    /**
     * Example statement:
     *      ".transition( () -> robot.intakeTouchSensor.hasTouched())"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state in linear order.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the next state in linear order.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     */
    public StateMachineBuilder transition(TransitionCondition condition, CallbackBase exitAction) {
        stateList.get(stateList.size()-1).getTransitions().add(new Triple<>(condition, null, exitAction)); // add transition to the last state
        return this;
    }

    public StateMachineBuilder transition(TransitionCondition condition) {
        stateList.get(stateList.size()-1).getTransitions().add(new Triple<>(condition, null, null)); // add transition to the last state
        return this;
    }

    /**
     *
     * @param time Indicates the amount of seconds it should wait before moving to the pointer state.
     * @param nextState Next state after the indicated time.
     */
    /*public StateMachineBuilder transitionTimed(double time, Enum nextState) {
        return transition(new TransitionTimed(time), nextState);
    }*/
    /**
     * Next state is determined by linear state order.
     * @param time Indicates the amount of seconds it should wait before moving to the pointer state.
     */
    /*public StateMachineBuilder transitionTimed(double time) {
        return transition(new TransitionTimed(time));
    }*/

    /**
     * Example:
     *      ".onEnter( () -> {
     *          robot.slides.armsIn();
     *          hasTransferred = true;
     *          robot.slides.extend();
     *      })
     *
     * @param call Segment of code that should be executed on the entrance of the state.
     */
    public StateMachineBuilder onEnter(CallbackBase call) {
        stateList.get(stateList.size()-1).setEnterActions(call);
        return this;
    }

    /**
     * This should only be used ONCE ideally at the end of the builder.
     *
     * @param call Segment of code that should be executed every loop call.
     */
    public StateMachineBuilder loopUpdates(CallbackBase call) {
        this.update = call;
        useUpdateConstructor = true;
        return this;
    }

    /**
     * Call this at the end of the StateMachine methods list
     *
     * Example:
     *      "...
     *      .build();"
     *
     * @return StateMachine object with the stateList and the updates list.
     */
    public StateMachine build() {
        if (useUpdateConstructor) {
            return new StateMachine(stateList, update);
        } else {
            return new StateMachine(stateList);
        }
    }
}
