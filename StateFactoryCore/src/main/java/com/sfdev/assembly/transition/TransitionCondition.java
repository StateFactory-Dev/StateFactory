package com.sfdev.assembly.transition;

import com.sfdev.assembly.callbacks.CallbackBase;

/**
 * Holds the boolean condition that determines if a state should transition or not.
 */
@FunctionalInterface
public interface TransitionCondition {
    /**
     * The actual boolean condition.
     * @return Returns the value of the condition (if it has been met or not).
     */
    boolean shouldTransition();
}
