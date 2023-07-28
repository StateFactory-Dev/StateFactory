package com.sfdev.assembly.state;


import com.sfdev.assembly.callbacks.CallbackBase;
import com.sfdev.assembly.transition.TransitionCondition;

import java.util.List;

/**
 * Represents each state and all of its properties
 */

public class State {
    private Enum name; // taking in the enum constant
    private CallbackBase enterActions;
    private CallbackBase exitActions;
    private List<Triple<TransitionCondition, Enum, CallbackBase>> transitions; // Pair: [TransitionCondition, Enum]
    private boolean isFailsafe;

    public State(Enum name, CallbackBase enterActions, CallbackBase exitActions, List<Triple<TransitionCondition, Enum, CallbackBase>> transitions) {
        this.name = name;
        this.enterActions = enterActions;
        this.exitActions = exitActions;
        this.transitions = transitions;
        isFailsafe = false;
    }

    public State(Enum name, CallbackBase enterActions,CallbackBase exitActions, List<Triple<TransitionCondition, Enum, CallbackBase>> transitions, boolean isFailsafe) {
        this.name = name;
        this.enterActions = enterActions;
        this.exitActions = exitActions;
        this.transitions = transitions;
        this.isFailsafe = false;
    }

    public Enum getName() {
        return name;
    }

    public String getNameAsString() {
        return name.toString();
    }

    public CallbackBase getEnterActions() {
        return enterActions;
    }

    public void setEnterActions(CallbackBase actions) {
        enterActions = actions;
    }

    public List<Triple<TransitionCondition, Enum, CallbackBase>> getTransitions() {
        return transitions;
    }

    public boolean isFailsafe() {
        return isFailsafe;
    }

    public CallbackBase getExitActions() {
        return exitActions;
    }

    public void setExitActions(CallbackBase exitActions) {
        this.exitActions = exitActions;
    }
}
