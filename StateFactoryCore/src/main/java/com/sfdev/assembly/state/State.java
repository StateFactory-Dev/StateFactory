package com.sfdev.assembly.state;


import com.sfdev.assembly.callbacks.CallbackBase;
import com.sfdev.assembly.transition.TransitionCondition;
import com.sfdev.assembly.transition.TransitionData;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents each state and all of its properties
 */

public class State {
    private String name; // taking in the enum constant
    private Enum nameEnum;
    private List<CallbackBase> enterActions;
    private List<CallbackBase> exitActions;
    private List<CallbackBase> loopActions;
    private List<TransitionData> transitions;
    private boolean isFailsafe;
    private TransitionCondition overallMinTransition;

    protected State(Enum name, boolean isFailsafe) {
        this(name.name(), isFailsafe);
        this.nameEnum = name;
    }

    /**
     * Creates the state object
     * @param name The name that is assigned to the state that is being created.
     * @param isFailsafe Determines whether the state should be determined as failsafe or not
     */
    protected State(String name, boolean isFailsafe) {
        this.name = name;
        enterActions = null;
        exitActions = null;
        loopActions = null;
        overallMinTransition = null;
        transitions = new ArrayList<>();
        this.isFailsafe = isFailsafe;
    }

    protected State(Enum nameEnum) {
        this(nameEnum, false);
        this.nameEnum = nameEnum;
    }

    protected State(String name) {
        this(name, false);
    }

    /**
     * Gets the name of the state.
     * @return Returns the of the state in the form of an Object.
     */
    public Enum getNameEnum() {
        return nameEnum;
    }

    /**
     * Gets the name of the state.
     * @return Returns the of the state in string form.
     */
    public String getNameString() {
        return name;
    }

    /**
     * Gets the CallbackBase containing the state enter actions.
     * @return Returns the callback containing the state enter actions.
     */
    protected List<CallbackBase> getEnterActions() {
        return enterActions;
    }

    /**
     * Sets the state's enter action.
     * @param actions The CallbackBase array that is to be the states new enterAction.
     */
    protected void setEnterActions(List<CallbackBase> actions) {
        enterActions = actions;
    }

    /**
     * Adds an enter action to the array
     * @param actions The CallbackBase that is to be the states new enter action.
     */
    protected void addEnterActions(CallbackBase actions) {
        if(enterActions == null) enterActions = new ArrayList<>();
        enterActions.add(actions);
    }

    /**
     * Gets the transition array.
     * @return Returns the array holding all the transitions that are to be assigned to this state. Made up of triples that hold the condition, pointer (if one is specified), and override exit action (if one is specified).
     */
    protected List<TransitionData> getTransitions() {
        return transitions;
    }

    /**
     * Sets the transition data.
     * @param data Takes in the TransitionData object to assign to the state.
     */
    protected void setTransitions(List<TransitionData> data) {
        transitions = data;
    }
    /**
     * Gets the loop actions.
     * @return Returns the callback containing the states loop actions.
     */
    protected List<CallbackBase> getLoopActions() {
        return loopActions;
    }

    /**
     * Sets the loop actions.
     * @param actions The CallBackBase that will be set as the new loop action.
     */
    protected void setLoopActions(List<CallbackBase> actions) {
        loopActions = actions;
    }

    /**
     * Add a loop action to the array
     * @param actions The CallbackBase that is to be the states new loop action.
     */
    protected void addLoopActions(CallbackBase actions) {
        if(loopActions == null) loopActions = new ArrayList<>();
        loopActions.add(actions);
    }

    /**
     * Get the boolean that specifies if the state is a fallback or not.
     * @return Returns the valus of isFailsafe.
     */
    protected boolean isFailsafe() {
        return isFailsafe;
    }

    /**
     * Gets the CallbackBase containing the state's exit actions.
     * @return Returns the callback containing the state's exit actions.
     */
    protected List<CallbackBase> getExitActions() {
        return exitActions;
    }

    /**
     * Sets the state's exit action.
     * @param exitActions The CallbackBase that is to be the states new exitAction.
     */
    protected void setExitActions(List<CallbackBase> exitActions) {
        this.exitActions = exitActions;
    }

    /**
     * Adds an exit action to the array
     * @param actions The CallbackBase that is to be the states new exit action.
     */
    protected void addExitAction(CallbackBase actions) {
        if(exitActions == null) exitActions = new ArrayList<>();
        exitActions.add(actions);
    }

    /**
     * Sets the minimum transition.
     * @param time The time that the transition should last for.
     */
    protected void setMinTransition(TransitionCondition time) {
        overallMinTransition = time;
    }

    /**
     * Gets the minimum transition.
     * @return Returns the transition condition that represents the minimum transition.
     */
    public TransitionCondition getMinTransition() {
        return overallMinTransition;
    }
}

