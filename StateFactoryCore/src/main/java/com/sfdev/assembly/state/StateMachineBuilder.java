package com.sfdev.assembly.state;

import com.sfdev.assembly.callbacks.CallbackBase;
import com.sfdev.assembly.callbacks.TimedCallback;
import com.sfdev.assembly.transition.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Builds each state for the StateMachine.
 */
public class StateMachineBuilder {
    private final List<State> stateList = new ArrayList<>();
    private List<String> stateSelect = new ArrayList<>();
    private boolean inStateSelection = false;
    private final WaitState.WAIT[] waitList = WaitState.WAIT.values();
    private int waitCounter = 0;

    private enum StateBuilder {
        STATE_BUILDER_ENUM
    }

    public StateMachineBuilder() {
        createState(new State(StateBuilder.STATE_BUILDER_ENUM));
    }

    /**
     * Creates a state through a given state object
     */
    public void createState(State state) {
        if(!stateList.isEmpty() && stateList.get(0).getNameString().equals(StateBuilder.STATE_BUILDER_ENUM.name())) {
            stateList.set(0, state);
        } else {
            stateList.add(state);
        }
    }

    /**
     * Creates a new state with an option to specify if the state is a fallback state or not.
     * In a fallback state, you MUST point to another state when transitioning & the only way to enter is via a transition pointing to the (fallback) state.
     *
     * @param stateName  Provides a string to represent the state being created.
     * @param isFailsafe Indicates to the state machine that the current state is a fallback state. This means it will be ignored when traversing from state to state in a linear order.
     */
    public StateMachineBuilder state(String stateName, boolean isFailsafe) { // initializing the state
        clearStateSelection();
        createState(new State(stateName, isFailsafe));
        return this;
    }

    /**
     * Creates a new state with an option to specify if the state is a fallback state or not.
     * In a fallback state, you MUST point to another state when transitioning & the only way to enter is via a transition pointing to the (fallback) state.
     *
     * @param stateName  Provides an enum to represent the state being created.
     * @param isFailsafe Indicates to the state machine that the current state is a fallback state. This means it will be ignored when traversing from state to state in a linear order.
     */
    public StateMachineBuilder state(Enum stateName, boolean isFailsafe) { // initializing the state
        clearStateSelection();
        createState(new State(stateName, isFailsafe));

        return this;
    }

    /**
     * Creates a state with values from a given state template.
     * @param name Provides an enum to represent the state being created.
     * @param state Provides the values and properties of the state.
     */
    public StateMachineBuilder stateTemplate(Enum name, State state) {
        createState(new State(name, state));

        return this;
    }

    /**
     * Creates a state with values from a given state template.
     * @param name Provides a string to represent the state being created.
     * @param state Provides the values and properties of the state.
     */
    public StateMachineBuilder stateTemplate(String name, State state) {
        createState(new State(name, state));

        return this;
    }


    /**
     * Creates a new state.
     *
     * @param stateName Provides an enum constant to represent the state being created.
     */
    public StateMachineBuilder state(Enum stateName) { // initializing the state
        state(stateName, false);

        return this;
    }

    /**
     * Creates a new state.
     *
     * @param stateName Provides a string to represent the state being created.
     */
    public StateMachineBuilder state(String stateName) { // initializing the state
        state(stateName, false);

        return this;
    }

    /**
     * Creates a new state with an option to specify if the state is a fallback state or not.
     * In a fallback state, you MUST point to another state when transitioning & the only way to enter is via a transition pointing to the (fallback) state.
     *
     * @param stateName Provides an enum constant to represent the state being created.
     */
    public StateMachineBuilder failsafeState(Enum stateName) { // initializing the state
        state(stateName, true);

        return this;
    }

    /**
     * Creates a new state with an option to specify if the state is a fallback state or not.
     * In a fallback state, you MUST point to another state when transitioning & the only way to enter is via a transition pointing to the (fallback) state.
     *
     * @param stateName Provides string to represent the state being created.
     */
    public StateMachineBuilder failsafeState(String stateName) { // initializing the state
        state(stateName);

        return this;
    }

    /**
     * Creating all different combinations of the wait states with potential pointers.
     * @param name The name of the wait state.
     * @param seconds Seconds to wait.
     */
    public StateMachineBuilder waitState(String name, double seconds) {
        state(name);
        transitionTimed(seconds);

        return this;
    }

    /**
     * Creating all different combinations of the wait states with potential pointers.
     * @param stateName The name of the wait state.
     * @param seconds Seconds to wait.
     */
    public StateMachineBuilder waitState(Enum stateName, double seconds) {
        state(stateName);
        transitionTimed(seconds);

        return this;
    }

    /**
     * Creating all different combinations of the wait states with potential pointers.
     * @param stateName The name of the wait state.
     * @param seconds Seconds to wait.
     * @param pointer The pointer state after the wait state.
     */
    public StateMachineBuilder waitState(Enum stateName, double seconds, Enum pointer) {
        state(stateName);
        transitionTimed(seconds, pointer);

        return this;
    }

    /**
     * Creating all different combinations of the wait states with potential pointers.
     * @param stateName The name of the wait state.
     * @param seconds Seconds to wait.
     * @param pointer The pointer state after the wait state.
     */
    public StateMachineBuilder waitState(String stateName, double seconds, String pointer) {
        state(stateName);
        transitionTimed(seconds, pointer);

        return this;
    }

    /**
     * Creating all different combinations of the wait states with potential pointers.
     * @param stateName The name of the wait state.
     * @param seconds Seconds to wait.
     * @param pointer The pointer state after the wait state.
     */
    public StateMachineBuilder waitState(String stateName, double seconds, Enum pointer) {
        state(stateName);
        transitionTimed(seconds, pointer);

        return this;
    }

    /**
     * Creating all different combinations of the wait states with potential pointers.
     * @param stateName The name of the wait state.
     * @param seconds Seconds to wait.
     * @param pointer The pointer state after the wait state.
     */
    public StateMachineBuilder waitState(Enum stateName, double seconds, String pointer) {
        state(stateName);
        transitionTimed(seconds, pointer);

        return this;
    }

    /**
     * Progresses to the next state after a certain amount of time. (non-blocking)
     *
     * @param seconds The amount of seconds to wait before moving to the indicated state.
     */
    public StateMachineBuilder waitState(double seconds, String pointer) {
        state(waitList[waitCounter]);
        waitCounter++;
        transitionTimed(seconds, pointer);

        return this;
    }

    /**
     * Progresses to the next state after a certain amount of time. (non-blocking)
     *
     * @param seconds The amount of seconds to wait before moving to the next state.
     */
    public StateMachineBuilder waitState(double seconds) {
        waitState(seconds, (String) null);

        return this;
    }

    /**
     * Progresses to the next state after a certain amount of time. (non-blocking)
     *
     * @param seconds The amount of seconds to wait before moving to the indicated state.
     */
    public StateMachineBuilder waitState(double seconds, Enum pointer) {
        waitState(seconds, pointer.name());

        return this;
    }

    /**
     * Progresses to the next state after a certain amount of time. (non-blocking)
     *
     * @param seconds The amount of seconds to wait before moving to the indicated state.
     */
    public StateMachineBuilder waitStatePointer(double seconds, Enum pointer) {
        waitState(seconds, pointer);

        return this;
    }

    /**
     * Progresses to the next state after a certain amount of time. (non-blocking)
     *
     * @param seconds The amount of seconds to wait before moving to the indicated state.
     */
    public StateMachineBuilder waitStatePointer(double seconds, String pointer) {
        waitState(seconds, pointer);

        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     * ".transition( () -> robot.intakeTouchSensor.hasTouched(), Enums.IntakeTransfer, ()-> robot.intake.retract() )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition  Indicates to the state machine under what condition it should transition to the nextState.
     *                   This is in the form of a lambda function that returns true or false depending on what the user requests.
     * @param nextState  Indicates what the state the StateMachine should transition to after the condition is true.
     * @param exitAction Tells the StateMachine to override the previously set exitAction if the condition is true.
     */
    public StateMachineBuilder transition(TransitionCondition condition, Enum nextState, CallbackBase exitAction) { // adding the new transition condition & next state
        transition(condition, nextState.name(), exitAction);

        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     * ".transition( () -> robot.intakeTouchSensor.hasTouched(), "IntakeTransfer", ()-> robot.intake.retract() )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition  Indicates to the state machine under what condition it should transition to the nextState.
     *                   This is in the form of a lambda function that returns true or false depending on what the user requests.
     * @param nextState  Indicates what the state the StateMachine should transition to after the condition is true.
     * @param exitAction Tells the StateMachine to override the previously set exitAction if the condition is true.
     */
    public StateMachineBuilder transition(TransitionCondition condition, String nextState, CallbackBase exitAction) { // adding the new transition condition & next state
        if(inStateSelection) {
            for (State currState : stateList) {
                if (stateSelect.contains(currState.getNameString())) {
                    currState.getTransitions().add(new TransitionData(condition, nextState, exitAction)); // add transition to the last state
                }
            }
        }
        else
            stateList.get(stateList.size() - 1).getTransitions().add(new TransitionData(condition, nextState, exitAction)); // add transition to the last state

        return this;
    }

    /**
     * Adds a minimum time to the state to make sure the state does not proceed without this amount of time passing.
     * This method applies to ALL transitions.e
     * @param time The amount of time that must pass before the state transitions.
     */
    public StateMachineBuilder minimumTransitionTimed(double time) {
        State list = stateList.get(stateList.size() - 1);
        list.setMinTransition(new TransitionTimed(time));

        return this;
    }

    /**
     * Adds a minimum time to the state to make sure the state does not proceed without this amount of time passing.
     * Only applies to the specified transitions (1-indexed).
     * @param time The amount of time that must pass before the state transitions.
     */
    public StateMachineBuilder minimumTransitionTimed(double time, int... transitionNumber) {
        State list = stateList.get(stateList.size() - 1);
        for (int i = 0; i < list.getTransitions().size(); i++) {
            for (int k : transitionNumber) {
                if(k > list.getTransitions().size()) {
                    throw new IllegalMinimumTransition("State " + list.getNameString() + ": Minimum transition on a non-existent state");
                }
                if (i+1 == k) {
                    list.getTransitions().get(i).setMinimumTransition(new TransitionTimed(time));
                    break;
                }
            }
        }


        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     * ".transition( () -> robot.intakeTouchSensor.hasTouched())"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state in linear order.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the next state in linear order.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     */
    public StateMachineBuilder transition(TransitionCondition condition) {
        transition(condition, (String) null, null);

        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     * ".transition( () -> robot.intakeTouchSensor.hasTouched(),() -> robot.intake.retract() )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition  Indicates to the state machine under what condition it should transition to the nextState.
     *                   This is in the form of a lambda function that returns true or false depending on what the user requests.
     * @param exitAction Tells the StateMachine to override the previously set exitAction if the condition is true.
     */
    public StateMachineBuilder transition(TransitionCondition condition, CallbackBase exitAction) {
        transition(condition, (String) null, exitAction);

        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     * ".transitionWithExitAction( () -> robot.intakeTouchSensor.hasTouched(),() -> robot.intake.retract() )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition  Indicates to the state machine under what condition it should transition to the nextState.
     *                   This is in the form of a lambda function that returns true or false depending on what the user requests.
     * @param exitAction Tells the StateMachine to override the previously set exitAction if the condition is true.
     */
    public StateMachineBuilder transitionWithExitAction(TransitionCondition condition, CallbackBase exitAction) {
        transition(condition, exitAction);

        return this;
    }


    /**
     * Assigns a new transition to a state.
     * Example statement:
     * ".transition( () -> robot.intakeTouchSensor.hasTouched(), Enums.IntakeTransfer )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the nextState.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     * @param nextState Indicates what the state the StateMachine should transition to after the condition is true.
     */
    public StateMachineBuilder transition(TransitionCondition condition, Enum nextState) {
        transition(condition, nextState, null);

        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     * ".transition( () -> robot.intakeTouchSensor.hasTouched(), Enums.IntakeTransfer )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the nextState.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     * @param nextState Indicates what the state the StateMachine should transition to after the condition is true.
     */
    public StateMachineBuilder transition(TransitionCondition condition, String nextState) {
        transition(condition, nextState, null);

        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     * ".transitionWithPointerState( () -> robot.intakeTouchSensor.hasTouched(), "IntakeTransfer" )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the nextState.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     * @param nextState Indicates what the state the StateMachine should transition to after the condition is true.
     */
    public StateMachineBuilder transitionWithPointerState(TransitionCondition condition, Enum nextState) {
        transition(condition, nextState);

        return this;
    }

    /**
     * Assigns a new transition to a state.
     * Example statement:
     * ".transitionWithPointerState( () -> robot.intakeTouchSensor.hasTouched(), "IntakeTransfer" )"
     * In this transition, the user inputted a condition: whether the intake touch sensor has been touched or not, and provided a pointer state.
     * When this condition turns true, the states exit actions will be executed and will transition to the next state.
     *
     * @param condition Indicates to the state machine under what condition it should transition to the nextState.
     *                  This is in the form of a lambda function that returns true or false depending on what the user requests.
     * @param nextState Indicates what the state the StateMachine should transition to after the condition is true.
     */
    public StateMachineBuilder transitionWithPointerState(TransitionCondition condition, String nextState) {
        transition(condition, nextState);

        return this;
    }

    /**
     * @param time      Indicates the amount of seconds it should wait before moving to the pointer state.
     * @param nextState Next state after the indicated time.
     */
    public StateMachineBuilder transitionTimed(double time, Enum nextState) {
        transition(new TransitionTimed(time), nextState);

        return this;
    }

    /**
     * @param time      Indicates the amount of seconds it should wait before moving to the pointer state.
     * @param nextState Next state after the indicated time.
     */
    public StateMachineBuilder transitionTimed(double time, String nextState) {
        transition(new TransitionTimed(time), nextState);

        return this;
    }

    /**
     * Next state is determined by linear state order.
     *
     * @param time Indicates the amount of seconds it should wait before moving to the pointer state.
     */
    public StateMachineBuilder transitionTimed(double time) {
        return transition(new TransitionTimed(time));
    }

    /**
     * @param time        Indicates the amount of seconds it should wait before moving to the pointer state.
     * @param nextState   Indicates the pointer state.
     * @param exitActions Indicates actions to execute after the indicated time passes.
     */
    public StateMachineBuilder transitionTimed(double time, Enum nextState, CallbackBase exitActions) {
        return transition(new TransitionTimed(time), nextState, exitActions);
    }

    /**
     * @param time        Indicates the amount of seconds it should wait before moving to the pointer state.
     * @param nextState   Indicates the pointer state.
     * @param exitActions Indicates actions to execute after the indicated time passes.
     */
    public StateMachineBuilder transitionTimed(double time, String nextState, CallbackBase exitActions) {
        return transition(new TransitionTimed(time), nextState, exitActions);
    }


    /**
     * @param time        Indicates the amoutn of seconds it should wait before moving to the pointer state.
     * @param exitActions Indicates actions to execute after the indicated time passes.
     * @return
     */
    public StateMachineBuilder transitionTimed(double time, CallbackBase exitActions) {
        return transition(new TransitionTimed(time), exitActions);
    }

    /**
     * Assigns an action to execute upon entering a state.
     * Example:
     * ".onEnter( () -> {
     * robot.slides.armsIn();
     * hasTransferred = true;
     * robot.slides.extend();
     * })
     *
     * @param call Segment of code that should be executed on the entrance of the state.
     */
    public StateMachineBuilder onEnter(CallbackBase call) {
        if(inStateSelection) {
            for (State currState : stateList) {
                if (stateSelect.contains(currState.getNameString())) {
                    currState.addEnterActions(call);
                }
            }
        }
        else
            stateList.get(stateList.size() - 1).addEnterActions(call);
        return this;
    }

    /**
     * Assigns an action to execute upon exiting a state.
     * Example:
     * ".onExit( () -> {
     * robot.slides.armsIn();
     * hasTransferred = true;
     * robot.slides.extend();
     * })
     *
     * @param call Segment of code that should be executed on the exit of the state, unless overridden by a transition.
     */
    public StateMachineBuilder onExit(CallbackBase call) {
        if(inStateSelection) {
            for (State currState : stateList) {
                if (stateSelect.contains(currState.getNameString())) {
                    currState.addExitAction(call);
                }
            }
        }
        else
            stateList.get(stateList.size() - 1).addExitAction(call);
        return this;
    }

    public StateMachineBuilder afterTime(double time, CallbackBase callbackBase) {
        if(inStateSelection) {
            for (State currState : stateList) {
                if (stateSelect.contains(currState.getNameString())) {
                    currState.addTimedAction(new TimedCallback(time, callbackBase));
                }
            }
        }
        else
            stateList.get(stateList.size() - 1).addTimedAction(new TimedCallback(time, callbackBase));

        return this;
    }

    /**
     * @param call Segment of code that will be executed every loop.
     */
    public StateMachineBuilder loop(CallbackBase call) {
        if(inStateSelection) {
            for (State currState : stateList) {
                if (stateSelect.contains(currState.getNameString())) {
                    currState.addLoopActions(call);
                }
            }
        }
        else
            stateList.get(stateList.size() - 1).addLoopActions(call);
        return this;
    }

    /**
     * Allows you to add enter, exit, and loop calls to the selected String states. Also allows you to add transitions.
     * @param states The states, defined by strings, to have the following actions added to.
     */
    public StateMachineBuilder states(String... states) {
        stateSelect = Arrays.asList(states);
        inStateSelection = true;

        return this;
    }

    /**
     * Allows you to add enter, exit, and loop calls to the selected Enum states. Also allows you to add transitions.
     * @param states The states, defined by enums, to have the following actions added to.
     */
    public StateMachineBuilder states(Enum... states) {
        List<String> stateNames = new ArrayList<>();
        for (Enum state : states) {
            stateNames.add(state.name());
        }

        stateSelect = stateNames;
        inStateSelection = true;

        return this;
    }

    /**
     * Call this at the end of the StateMachine methods list to construct the machine.
     * Example:
     * "...
     * .build();"
     *
     * @return StateMachine object with the stateList and the updates list.
     */
    public StateMachine build() {
        // Order timedCallbacks
        for (State state : stateList) {
            if (!state.getTimedAction().isEmpty()) state.getTimedAction().sort((a,b) -> a.getTime() >= b.getTime() ? 1 : -1);
        }

        return new StateMachine(stateList);
    }

    /**
     * Call this at the end of the StateMachine methods list to construct a state template.
     * Example:
     * "...
     * .buildStateTemplate();"
     *
     * @return State object with the appropriate properties.
     */
    public State buildStateTemplate() {
        if (stateList.get(0).getNameString().equals(StateBuilder.STATE_BUILDER_ENUM.name())) {
            return stateList.get(0);
        } else throw new StateTemplateBuilderException("Attempted to build a state template with normal state machine builder. Do not indicate state name with '.state()' or change '.buildStateTemplate()' to '.build()'");
    }

    /**
     * Clears the stateSelect array
     */
    private void clearStateSelection() {
        if(stateSelect != null && !stateSelect.isEmpty()) stateSelect = null;
        inStateSelection = false;
    }
}