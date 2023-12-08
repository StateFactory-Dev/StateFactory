package com.sfdev.assembly.state;

import com.sfdev.assembly.callbacks.CallbackBase;
import com.sfdev.assembly.transition.TransitionCondition;
import com.sfdev.assembly.transition.TransitionTimed;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

abstract class StateMachineBuilderException extends RuntimeException {
    public StateMachineBuilderException(String s, Throwable stackTrace) {
        super(s, stackTrace);
    }
}
class StateMachineTransitionException extends StateMachineBuilderException { public StateMachineTransitionException(String s, Throwable stackTrace) { super(s, stackTrace); } }
class InvalidStateException extends StateMachineBuilderException { public InvalidStateException(String s, Throwable stackTrace) { super(s, stackTrace); } }

/**
 * Manages all states and transitions between them.
 */
public class StateMachine {
    // linear list and fallback list logic
    public List<State> linearList = new ArrayList<>();
    private List<State> fallbackList = new ArrayList<>();
    private CallbackBase update;
    private HashMap<Enum, Integer> linearPlacements = new HashMap<>();
    private HashMap<Enum, Integer> fallbackPlacements = new HashMap<>();

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

        // splitting list stateList between linearList and fallbackList
        for(State s : stateList) {
            if(s.isFailsafe())
                fallbackList.add(s);
            else
                linearList.add(s);
        }

        for(State s : linearList) {
            linearPlacements.put(s.getName(), linearList.indexOf(s));
        }

        for(State s : fallbackList) {
            fallbackPlacements.put(s.getName(), fallbackList.indexOf(s));
        }

        currentState = linearList.get(0);
    }


    /**
     * Constructs a new machine with a list of updates to iterate over.
     * @param stateList Provides the list of states for the StateMachine to parse and perform logic with.
     * @param update A set of commands that a user may want to execute in ever loop segment. For example, a lockTo() function.
     */
    public StateMachine(List<State> stateList, CallbackBase update) {
        this.update = update;

        // splitting list stateList between linearList and fallbackList
        for(State s : stateList) {
            if(s.isFailsafe())
                fallbackList.add(s);
            else
                linearList.add(s);
        }

        for(State s : linearList) {
            linearPlacements.put(s.getName(), linearList.indexOf(s));
        }

        for(State s : fallbackList) {
            fallbackPlacements.put(s.getName(), fallbackList.indexOf(s));
        }

        currentState = linearList.get(0);
        useUpdate = true;
    }

    /**
     * Gets the current state's name in enum type.
     * @return Enum constant of the current state
     */
    public Enum getState() {
        return currentState.getName();
    }

    /**
     * Starts the state machine - executes enter actions and starts all timed transitions for the current state.
     */
    public void start() {
        isRunning = true;

        if(currentState.getEnterActions() != null) {
            currentState.getEnterActions().call();
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
            currentState = linearList.get(linearList.indexOf(state));
        } catch (NullPointerException e) {
            try {
                currentState = fallbackList.get(fallbackList.indexOf(state));
            } catch (NullPointerException m) {
                throw new InvalidStateException("Invalid state indicated. Ensure that the given enum is connected to a state.", m);
            }
        }
        currentState = linearList.get(linearList.indexOf(state));
    }

    private CallbackBase exitAction;

    /**
     * Should be called in every loop. Executes timed transitions and performs transitions.
     */
    public void update() {
        if(!isRunning) return;
        if(currentState.getTransitions().isEmpty() && currentState.getName() != StateMachineBuilder.WAIT.TEMP) {
            stop();
        }

        for (Triple<TransitionCondition, Enum, CallbackBase> transitionInfo : currentState.getTransitions()) {
            if(transitionInfo.first instanceof TransitionTimed && !((TransitionTimed) transitionInfo.first).timerStarted()) { // starting all timedTransitions
                ((TransitionTimed) transitionInfo.first).startTimer();
            }

            int currIndex = linearPlacements.get(currentState.getName());

            if (transitionInfo.getFirst().shouldTransition()) {

                exitAction = transitionInfo.getThird(); // setting exit actions

                if(transitionInfo.getSecond() != null) { // has a pointer
                    try { // try grabbing target state from either linear or failsafes
                        nextState = linearList.get(linearPlacements.get(transitionInfo.getSecond())); // linearPlacements throws null if it does not exist which throws NPE
                    } catch (NullPointerException e) {
                        try {
                            nextState = fallbackList.get(fallbackPlacements.get(transitionInfo.getSecond()));
                        } catch (NullPointerException m) {
                            throw new InvalidStateException("Invalid state indicated. Ensure that the pointer enum is connected to a state.", m);
                        }
                    }
                } else { // linear order
                    try {
                        nextState = linearList.get(currIndex + 1);
                    } catch (IndexOutOfBoundsException e) {
                        throw new StateMachineTransitionException("Transition Indicated, But No Next State Found. Remove final case transition statement.", e);
                    }
                }
                willTransition = true;
                break;
            }
        }

        // calling loop actions
        if(currentState.getLoopActions() != null) {
            currentState.getLoopActions().call();
        }

        if (!hasEntered && currentState.getEnterActions() != null) { // perform enter action
            currentState.getEnterActions().call();
            hasEntered = true;
        }

        if (willTransition && exitAction != null) { // if transitioning, perform exit actions
            exitAction.call();
            currentState = nextState;
            hasEntered = false;
            willTransition = false;
            exitAction = null;

            // RESETTING all timed transitions
            for (Triple<TransitionCondition, Enum, CallbackBase> transitionInfo : currentState.getTransitions()) {
                if (transitionInfo.first instanceof TransitionTimed && ((TransitionTimed) transitionInfo.first).timerStarted()) {
                    ((TransitionTimed) transitionInfo.first).resetTimer();
//                    System.out.println("RESETTING ALL TIMERS");
                }
            }

        } else if (willTransition && exitAction == null) {

            if (currentState.getExitActions() != null) {
                currentState.getExitActions().call();
            }

            currentState = nextState;
            hasEntered = false;
            willTransition = false;
            exitAction = null;

            // RESETTING all timed transitions
            for (Triple<TransitionCondition, Enum, CallbackBase> transitionInfo : currentState.getTransitions()) {
                if (transitionInfo.first instanceof TransitionTimed && ((TransitionTimed) transitionInfo.first).timerStarted()) {
                    ((TransitionTimed) transitionInfo.first).resetTimer();
//                    System.out.println("RESETTING ALL TIMERS");
                }
            }
        }

        if (useUpdate) { // executing loop updates
            update.call();
        }
    }
}