package com.sfdev.assembly.state;

import android.telecom.Call;
import com.sfdev.assembly.callbacks.CallbackBase;
import com.sfdev.assembly.transition.TransitionCondition;
import com.sfdev.assembly.transition.TransitionData;
import com.sfdev.assembly.transition.TransitionTimed;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

abstract class StateMachineBuilderException extends RuntimeException {
    public StateMachineBuilderException(String s) {
        super(s);
    }
}
class StateMachineTransitionException extends StateMachineBuilderException { public StateMachineTransitionException(String s) { super(s); } }
class InvalidStateException extends StateMachineBuilderException { public InvalidStateException(String s) { super(s); } }
class StateNotEnumException extends StateMachineBuilderException { public StateNotEnumException(String s) { super(s); } }
class IllegalMinimumTransition extends StateMachineBuilderException { public IllegalMinimumTransition(String s) { super(s); } }
/**
 * Manages all states and transitions between them.
 */
public class StateMachine {
    // linear list and fallback list logic
    private List<State> linearList;
    private List<State> fallbackList;
    private List<List<State>> selectStates;
    private HashMap<String, Integer> linearPlacements;
    private HashMap<String, Integer> fallbackPlacements = new HashMap<>();
    State currentState;
    State nextState;
    private boolean willTransition = false;
    private boolean hasEntered = false;
    private boolean isRunning = false;
    private boolean useUpdate = false;


    /**
     * Constructs a new state machine.
     * @param stateList Provides the list of states for the StateMachine to parse and perform logic with.
     */
    public StateMachine( List<State> stateList) {
        linearList = new ArrayList<>();
        fallbackList = new ArrayList<>();
        selectStates = new ArrayList<>();

        linearPlacements = new HashMap<>();
        fallbackPlacements = new HashMap<>();

        // splitting list stateList between linearList and fallbackList
        for(State s : stateList) {
            if(s.isFailsafe())
                fallbackList.add(s);
            else
                linearList.add(s);
        }

        for(State s : linearList) {
            linearPlacements.put(s.getNameString(), linearList.indexOf(s));
        }

        for(State s : fallbackList) {
            fallbackPlacements.put(s.getNameString(), fallbackList.indexOf(s));
        }

        currentState = linearList.get(0);
    }

    /**
     * Gets the current state's name in enum type.
     * @return Enum constant of the current state
     */
    public Enum getStateEnum() {
        return currentState.getNameEnum();
    }

    /**
     * Gets the current state's name in a string.
     * @return String of the current state
     */
    public String getStateString() {
        return currentState.getNameString();
    }

    /**
     * Gets the current state's name in enum type. Throws an error if the state is a string state.
     * @return Enum constant of the current state
     */
    public Enum getState() {
        if(currentState.getNameEnum() == null) {
            throw new StateNotEnumException("All States Must Be An Enum For getState()");
        }

        return currentState.getNameEnum();
    }

    /**
     * Starts the state machine - executes enter actions and starts all timed transitions for the current state.
     */
    public void start() {
        isRunning = true;

        List<CallbackBase> enterActions = currentState.getEnterActions();

        if(enterActions != null && !enterActions.isEmpty()) {
            for(CallbackBase action : enterActions) action.call();
        }

        hasEntered = true;
    }

    /**
     * Stops the state machine. Potential usages lie in controlling the state machine through something else.
     */
    public void stop() {
        isRunning = false;
    }

    /**
     * Resets the machine - sets the current state to the first one and starts running.
     */
    public void reset() {
        currentState = linearList.get(0);
        nextState = null;
        isRunning = true;
    }

    /**
     *
     * @return Returns whether the state machine is running or not.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Starting the state machine at the indicated state
     * @param state Prematurely setting the statemachine to the indicated state
     */
    public void setState(Enum state) {
        try { // try grabbing target state from either linear or failsafes
            currentState = linearList.get(linearPlacements.get(state.name()));
        } catch (NullPointerException e) {
            try {
                currentState = fallbackList.get(fallbackPlacements.get(state.name()));
            } catch (NullPointerException m) {
                throw new InvalidStateException("Invalid state indicated. Ensure that the given enum is connected to a state.");
            }
        }
    }

    /**
     * Starting the state machine at the indicated state
     * @param state Prematurely setting the statemachine to the indicated state
     */
    public void setState(String state) {
        try { // try grabbing target state from either linear or failsafes
            currentState = linearList.get(linearPlacements.get(state));
        } catch (NullPointerException e) {
            try {
                currentState = fallbackList.get(fallbackPlacements.get(state));
            } catch (NullPointerException m) {
                throw new InvalidStateException("Invalid state indicated. Ensure that the given enum is connected to a state.");
            }
        }
    }


    /**
     * Should be called in every loop. Executes timed transitions and performs transitions.
     */
    public void update() {

        if(!isRunning) return;
        // Turning the state machine off at the correct state
        if(currentState.getTransitions().isEmpty() && currentState.getNameEnum() != StateMachineBuilder.WAIT.TEMP && currentState.getLoopActions() == null) {
            stop();
        }

        if (!hasEntered && currentState.getEnterActions() != null && !currentState.getEnterActions().isEmpty()) { // perform enter action
            for(CallbackBase action : currentState.getEnterActions()) action.call();
            hasEntered = true;
        }

        if (currentState.getMinTransition() != null && currentState.getMinTransition() instanceof TransitionTimed && !((TransitionTimed) currentState.getMinTransition()).timerStarted()) {
            ((TransitionTimed) currentState.getMinTransition()).startTimer();
        }

        for (TransitionData transitionInfo : currentState.getTransitions()) {
            transitionInfo.runTimer();

            if ((currentState.getMinTransition() == null && transitionInfo.shouldTransition()) ||
                    (currentState.getMinTransition() != null && currentState.getMinTransition().shouldTransition() && transitionInfo.shouldTransition())) {

                if(transitionInfo.getPointerState() != null) { // has a pointer
                    try { // try grabbing target state from either linear or failsafes
                        nextState = linearList.get(linearPlacements.get(transitionInfo.getPointerState())); // linearPlacements throws null if it does not exist which throws NPE
                    } catch (NullPointerException e) {
                        try {
                            nextState = fallbackList.get(fallbackPlacements.get(transitionInfo.getPointerState()));
                        } catch (NullPointerException m) {
                            throw new InvalidStateException("Invalid state indicated. Ensure that the pointer enum is connected to a state.");
                        }
                    }
                } else { // linear order
                    try {
                        int currIndex = linearPlacements.get(currentState.getNameString());
                        nextState = linearList.get(currIndex + 1);
                    } catch (IndexOutOfBoundsException e) {
                        throw new StateMachineTransitionException("Transition Indicated, But No Next State Found. Remove final case transition statement.");
                    }
                }

                if (transitionInfo.getExitAction() != null) {
                    transitionInfo.getExitAction().call();
                }

                willTransition = true;
                break;
            }
        }

        // calling loop actions
        if(currentState.getLoopActions() != null) {
            for(CallbackBase action : currentState.getLoopActions()) action.call();
        }

        if (willTransition && currentState.getExitActions() != null) { // if transitioning, perform exit actions
            for(CallbackBase action : currentState.getExitActions()) action.call();
        }

        if(willTransition) {

            // RESETTING all timed transitions
            for (TransitionData transitionInfo : currentState.getTransitions()) {
                transitionInfo.resetTimer();
            }

            if(currentState.getMinTransition() != null && currentState.getMinTransition() instanceof TransitionTimed) {
                ((TransitionTimed) currentState.getMinTransition()).resetTimer();
            }
            currentState = nextState;

            hasEntered = false;
            willTransition = false;
        }
    }
}