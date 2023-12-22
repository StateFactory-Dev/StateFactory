package com.sfdev.assembly.state;

import com.sfdev.assembly.callbacks.CallbackBase;
//import com.sfdev.assembly.transition.*;
import com.sfdev.assembly.transition.TransitionTimed;
import com.sfdev.assembly.transition.TransitionCondition;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Builds each state for the StateMachine.
 */
public class StateMachineBuilder { // takes in the enum of states
    private List<State> stateList = new ArrayList<>();
    private CallbackBase update;
    protected enum WAIT {
        TEMP;
    }

    private boolean useUpdateConstructor = false;

    /**
     * Creates a new state.
     * @param stateName Provides an enum constant to represent the state being created.
     */
    public StateMachineBuilder state(Enum stateName) { // initializing the state
        stateList.add(new State(stateName,null, null, null, new ArrayList<>(), false));
        return this;
    }

    /**
     * Creates a new state.
     * @param stateName Provides a string to represent the state being created.
     */
    public StateMachineBuilder state(String stateName) { // initializing the state
        stateList.add(new State(stateName,null, null, null, new ArrayList<>(), false));
        return this;
    }

    /**
     * Creates a new state with an option to specify if the state is a fallback state or not.
     * In a fallback state, you MUST point to another state when transitioning & the only way to enter is via a transition pointing to the (fallback) state.
     * @param stateName Provides a string to represent the state being created.
     * @param isFailsafe Indicates to the state machine that the current state is a fallback state. This means it will be ignored when traversing from state to state in a linear order.
     */
    public StateMachineBuilder state(Enum stateName, boolean isFailsafe) { // initializing the state
        stateList.add(new State(stateName, null, null, null, new ArrayList<>(), isFailsafe));
        return this;
    }

    /**
     * Creates a new state with an option to specify if the state is a fallback state or not.
     * In a fallback state, you MUST point to another state when transitioning & the only way to enter is via a transition pointing to the (fallback) state.
     * @param stateName Provides a string to represent the state being created.
     * @param isFailsafe Indicates to the state machine that the current state is a fallback state. This means it will be ignored when traversing from state to state in a linear order.
     */
    public StateMachineBuilder state(String stateName, boolean isFailsafe) { // initializing the state
        stateList.add(new State(stateName, null, null, null, new ArrayList<>(), isFailsafe));
        return this;
    }

    /**
     * Creates a new state with an option to specify if the state is a fallback state or not.
     * In a fallback state, you MUST point to another state when transitioning & the only way to enter is via a transition pointing to the (fallback) state.
     * @param stateName Provides an enum constant to represent the state being created.
     */
    public StateMachineBuilder failsafeState(Enum stateName) { // initializing the state
        stateList.add(new State(stateName, null, null, null, new ArrayList<>(), true));
        return this;
    }

    /**
     * Creates a new state with an option to specify if the state is a fallback state or not.
     * In a fallback state, you MUST point to another state when transitioning & the only way to enter is via a transition pointing to the (fallback) state.
     * @param stateName Provides string to represent the state being created.
     */
    public StateMachineBuilder failsafeState(String stateName) { // initializing the state
        stateList.add(new State(stateName, null, null, null, new ArrayList<>(), true));
        return this;
    }

    /**
     * Progresses to the next state after a certain amount of time. (non-blocking)
     * @param seconds The amount of seconds to wait before moving to the next state.
     *
     */
    public StateMachineBuilder waitState(double seconds) {
        State temp = new State(WAIT.TEMP, null, null, null, Arrays.asList(new Triple<>(new TransitionTimed(seconds), null, null)), false);
        stateList.add(temp);
        return this;
    }

    /**
     * Progresses to the next state after a certain amount of time. (non-blocking)
     * @param seconds The amount of seconds to wait before moving to the indicated state.
     *
     */
    public StateMachineBuilder waitState(double seconds, Enum pointer) {
        stateList.add(new State(WAIT.TEMP, null, null, null, Arrays.asList(new Triple<>(new TransitionTimed(seconds), pointer.name(), null)), false));
        return this;
    }

    /**
     * Progresses to the next state after a certain amount of time. (non-blocking)
     * @param seconds The amount of seconds to wait before moving to the indicated state.
     *
     */
    public StateMachineBuilder waitState(double seconds, String pointer) {
        stateList.add(new State(WAIT.TEMP, null, null, null, Arrays.asList(new Triple<>(new TransitionTimed(seconds), pointer, null)), false));
        return this;
    }

    /**
     * Progresses to the next state after a certain amount of time. (non-blocking)
     * @param seconds The amount of seconds to wait before moving to the indicated state.
     *
     */
    public StateMachineBuilder waitStatePointer(double seconds, Enum pointer) {
        stateList.add(new State(WAIT.TEMP, null, null, null, Arrays.asList(new Triple<>(new TransitionTimed(seconds), pointer.name(), null)), false));
        return this;
    }

    /**
     * Progresses to the next state after a certain amount of time. (non-blocking)
     * @param seconds The amount of seconds to wait before moving to the indicated state.
     *
     */
    public StateMachineBuilder waitStatePointer(double seconds, String pointer) {
        stateList.add(new State(WAIT.TEMP, null, null, null, Arrays.asList(new Triple<>(new TransitionTimed(seconds), pointer, null)), false));
        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     *      ".transition( () -> robot.intakeTouchSensor.hasTouched(), Enums.IntakeTransfer, ()-> robot.intake.retract() )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the nextState.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     *
     * @param nextState Indicates what the state the StateMachine should transition to after the condition is true.
     *
     * @param exitAction Tells the StateMachine to override the previously set exitAction if the condition is true.
     */
    public StateMachineBuilder transition(TransitionCondition condition, Enum nextState, CallbackBase exitAction) { // adding the new transition condition & next state
        stateList.get(stateList.size()-1).getTransitions().add(new Triple<>(condition, nextState.name(), exitAction)); // add transition to the last state
        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     *      ".transition( () -> robot.intakeTouchSensor.hasTouched(), "IntakeTransfer", ()-> robot.intake.retract() )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the nextState.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     *
     * @param nextState Indicates what the state the StateMachine should transition to after the condition is true.
     *
     * @param exitAction Tells the StateMachine to override the previously set exitAction if the condition is true.
     */
    public StateMachineBuilder transition(TransitionCondition condition, String nextState, CallbackBase exitAction) { // adding the new transition condition & next state
        stateList.get(stateList.size()-1).getTransitions().add(new Triple<>(condition, nextState, exitAction)); // add transition to the last state
        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     *      ".transition( () -> robot.intakeTouchSensor.hasTouched())"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state in linear order.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the next state in linear order.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     */
    public StateMachineBuilder transition(TransitionCondition condition) {
        stateList.get(stateList.size()-1).getTransitions().add(new Triple<>(condition, null, null)); // add transition to the last state
        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     *      ".transition( () -> robot.intakeTouchSensor.hasTouched(),() -> robot.intake.retract() )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the nextState.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     *
     * @param exitAction Tells the StateMachine to override the previously set exitAction if the condition is true.
     */
    public StateMachineBuilder transition(TransitionCondition condition, CallbackBase exitAction) {
        stateList.get(stateList.size()-1).getTransitions().add(new Triple<>(condition, null, exitAction)); // add transition to the last state
        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     *      ".transitionWithExitAction( () -> robot.intakeTouchSensor.hasTouched(),() -> robot.intake.retract() )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the nextState.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     *
     * @param exitAction Tells the StateMachine to override the previously set exitAction if the condition is true.
     */
    public StateMachineBuilder transitionWithExitAction(TransitionCondition condition, CallbackBase exitAction) {
        stateList.get(stateList.size()-1).getTransitions().add(new Triple<>(condition, null, exitAction)); // add transition to the last state
        return this;
    }


    /**
     * Assigns a new transition to a state.
     * Example statement:
     *      ".transition( () -> robot.intakeTouchSensor.hasTouched(), Enums.IntakeTransfer )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the nextState.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     *
     * @param nextState Indicates what the state the StateMachine should transition to after the condition is true.
     *
     */
    public StateMachineBuilder transition(TransitionCondition condition, Enum nextState) {
        stateList.get(stateList.size()-1).getTransitions().add(new Triple<>(condition, nextState.name(), null)); // add transition to the last state
        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     *      ".transition( () -> robot.intakeTouchSensor.hasTouched(), Enums.IntakeTransfer )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the nextState.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     *
     * @param nextState Indicates what the state the StateMachine should transition to after the condition is true.
     *
     */
    public StateMachineBuilder transition(TransitionCondition condition, String nextState) {
        stateList.get(stateList.size()-1).getTransitions().add(new Triple<>(condition, nextState, null)); // add transition to the last state
        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     *      ".transitionWithPointerState( () -> robot.intakeTouchSensor.hasTouched(), "IntakeTransfer" )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the nextState.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     *
     * @param nextState Indicates what the state the StateMachine should transition to after the condition is true.
     *
     */
    public StateMachineBuilder transitionWithPointerState(TransitionCondition condition, Enum nextState) {
        stateList.get(stateList.size()-1).getTransitions().add(new Triple<>(condition, nextState.name(), null)); // add transition to the last state
        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     *      ".transitionWithPointerState( () -> robot.intakeTouchSensor.hasTouched(), "IntakeTransfer" )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the nextState.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     *
     * @param nextState Indicates what the state the StateMachine should transition to after the condition is true.
     *
     */
    public StateMachineBuilder transitionWithPointerState(TransitionCondition condition, String nextState) {
        stateList.get(stateList.size()-1).getTransitions().add(new Triple<>(condition, nextState, null)); // add transition to the last state
        return this;
    }

    /**
     *
     * @param time Indicates the amount of seconds it should wait before moving to the pointer state.
     * @param nextState Next state after the indicated time.
     */
    public StateMachineBuilder transitionTimed(double time, Enum nextState) {
        return transition(new TransitionTimed(time), nextState);
    }

    /**
     *
     * @param time Indicates the amount of seconds it should wait before moving to the pointer state.
     * @param nextState Next state after the indicated time.
     */
    public StateMachineBuilder transitionTimed(double time, String nextState) {
        return transition(new TransitionTimed(time), nextState);
    }
    /**
     * Next state is determined by linear state order.
     * @param time Indicates the amount of seconds it should wait before moving to the pointer state.
     */
    public StateMachineBuilder transitionTimed(double time) {
        return transition(new TransitionTimed(time));
    }

    /**
     *
     * @param time Indicates the amount of seconds it should wait before moving to the pointer state.
     * @param nextState Indicates the pointer state.
     * @param exitActions Indicates actions to execute after the indicated time passes.
     */
    public StateMachineBuilder transitionTimed(double time, Enum nextState, CallbackBase exitActions) {
        return transition(new TransitionTimed(time), nextState, exitActions);
    }

    /**
     *
     * @param time Indicates the amount of seconds it should wait before moving to the pointer state.
     * @param nextState Indicates the pointer state.
     * @param exitActions Indicates actions to execute after the indicated time passes.
     */
    public StateMachineBuilder transitionTimed(double time, String nextState, CallbackBase exitActions) {
        return transition(new TransitionTimed(time), nextState, exitActions);
    }


    /**
     *
     * @param time Indicates the amoutn of seconds it should wait before moving to the pointer state.
     * @param exitActions Indicates actions to execute after the indicated time passes.
     * @return
     */
    public StateMachineBuilder transitionTimed(double time, CallbackBase exitActions) {
        return transition(new TransitionTimed(time), exitActions);
    }

    /**
     * Assigns an action to execute upon entering a state.
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
     * Assigns an action to execute upon exiting a state.
     * Example:
     *      ".onExit( () -> {
     *          robot.slides.armsIn();
     *          hasTransferred = true;
     *          robot.slides.extend();
     *      })
     *
     * @param call Segment of code that should be executed on the exit of the state, unless overridden by a transition.
     */
    public StateMachineBuilder onExit(CallbackBase call) {
        stateList.get(stateList.size()-1).setExitActions(call);
        return this;
    }

    /**
     *
     * @param call Segment of code that will be executed every loop.
     */
    public StateMachineBuilder loop(CallbackBase call) {
        stateList.get(stateList.size()-1).setLoopActions(call);
        return this;
    }

    /**
     * Assigns updates to be automatically called by the statemachine.
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
     * Call this at the end of the StateMachine methods list to construct the machine.
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
