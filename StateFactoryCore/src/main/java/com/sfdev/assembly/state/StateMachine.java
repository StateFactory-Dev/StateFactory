package com.sfdev.assembly.state;

import com.sfdev.assembly.callbacks.CallbackBase;
import com.sfdev.assembly.callbacks.TimedCallback;
import com.sfdev.assembly.transition.*;

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
class StateTemplateBuilderException extends StateMachineBuilderException { public StateTemplateBuilderException(String s) { super(s); } }
/**
 * Manages all states and transitions between them.
 */
public class StateMachine {
    // linear list and fallback list logic
    private final List<State> linearList;
    private final List<State> fallbackList;
    private final HashMap<String, Integer> linearPlacements;
    private final HashMap<String, Integer> fallbackPlacements;
    State currentState;
    State nextState;
    private boolean willTransition = false;
    private boolean hasEntered = false;
    private boolean isRunning = false;
    private boolean timedCallbacksDone = false;


    /**
     * Constructs a new state machine.
     * @param stateList Provides the list of states for the StateMachine to parse and perform logic with.
     */
    public StateMachine( List<State> stateList) {
        linearList = new ArrayList<>();
        fallbackList = new ArrayList<>();

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
        if (linearPlacements.get(state.name()) != null) { // try grabbing target state from either linear or failsafes
            currentState = linearList.get(linearPlacements.get(state.name()));
        } else {
            if(fallbackPlacements.get(state.name()) != null) {
                currentState = fallbackList.get(fallbackPlacements.get(state.name()));
            } else {
                throw new InvalidStateException("Set state \"" + state.name() + "\": Invalid state indicated. Ensure that the given enum is connected to a state.");
            }
        }
    }

    /**
     * Starting the state machine at the indicated state
     * @param state Prematurely setting the statemachine to the indicated state
     */
    public void setState(String state) {
        if (linearPlacements.get(state) != null) { // try grabbing target state from either linear or failsafes
            currentState = linearList.get(linearPlacements.get(state));
        } else {
            if(fallbackPlacements.get(state) != null) {
                currentState = fallbackList.get(fallbackPlacements.get(state));
            } else {
                throw new InvalidStateException("Invalid state indicated: \"" + state + "\". Ensure that the given string is connected to a state.");
            }
        }
    }


    /**
     * Should be called in every loop. Executes transitions and actions.
     */
    public void update() {

        if(!isRunning) return;
        // Turning the state machine off at the correct state
        if( (currentState.getTimedAction().isEmpty() || timedCallbacksDone) && currentState.getTransitions().isEmpty() && currentState.getLoopActions() == null) {
            stop();
            isRunning = false;
        }

        if (!hasEntered) {
            if(currentState.getEnterActions() != null && !currentState.getEnterActions().isEmpty()) { // perform enter action
                for (CallbackBase action : currentState.getEnterActions()) action.call();
            }
            hasEntered = true;
        }

        if (currentState.getMinTransition() != null && currentState.getMinTransition() instanceof TransitionTimed && !((TransitionTimed) currentState.getMinTransition()).timerStarted()) {
            ((TransitionTimed) currentState.getMinTransition()).startTimer();
        }

        for (TimedCallback timedCallback : currentState.getTimedAction()) {
            if (!timedCallback.timerStarted()) {
                timedCallback.startTimer();
            }

            timedCallback.call();

            timedCallbacksDone = timedCallback.isDone();
        }

        // calling loop actions
        if(currentState.getLoopActions() != null) {
            for(CallbackBase action : currentState.getLoopActions()) action.call();
        }

        for (TransitionData transitionInfo : currentState.getTransitions()) {
            transitionInfo.runTimer();

            if ((currentState.getMinTransition() == null && transitionInfo.shouldTransition()) ||
                    (currentState.getMinTransition() != null && currentState.getMinTransition().shouldTransition() && transitionInfo.shouldTransition())) {

                if(transitionInfo.getPointerState() != null) { // has a pointer
                    if (linearPlacements.get(transitionInfo.getPointerState()) != null) {
                        nextState = linearList.get(linearPlacements.get(transitionInfo.getPointerState())); // linearPlacements throws null if it does not exist which throws NPE
                    } else {
                        if(fallbackPlacements.get(transitionInfo.getPointerState()) != null) {
                            nextState = fallbackList.get(fallbackPlacements.get(transitionInfo.getPointerState()));
                        } else {
                            throw new InvalidStateException("State \"" + currentState.getNameString() + "\": Invalid state indicated: " + transitionInfo.getPointerState() + ". Ensure that the pointer enum is connected to a state.");
                        }
                    }
                } else { // linear order
                    int currIndex = linearPlacements.get(currentState.getNameString());
                    if(linearList.size() > currIndex+1) {
                        nextState = linearList.get(currIndex + 1);
                    } else {
                        throw new StateMachineTransitionException("State \"" + currentState.getNameString() + "\": Transition Indicated, But No Next State Found. Remove final case transition statement.");
                    }
                }

                if (transitionInfo.getExitAction() != null) {
                    transitionInfo.getExitAction().call();
                }

                willTransition = true;
                break;
            }
        }

        if (willTransition && currentState.getExitActions() != null) { // if transitioning, perform exit actions
            for(CallbackBase action : currentState.getExitActions()) action.call();
        }

        if(willTransition) {

            // RESETTING all timed transitions
            for (TransitionData transitionInfo : currentState.getTransitions()) {
                transitionInfo.resetTimer();
            }

            for(TimedCallback timedCallback : currentState.getTimedAction()) {
                timedCallback.resetTimer();
            }

            if(currentState.getMinTransition() != null && currentState.getMinTransition() instanceof TransitionTimed) {
                ((TransitionTimed) currentState.getMinTransition()).resetTimer();
            }
            currentState = nextState;

            hasEntered = false;
            willTransition = false;
            timedCallbacksDone = false;
        }
    }
}