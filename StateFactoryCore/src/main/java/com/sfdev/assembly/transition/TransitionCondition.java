package com.sfdev.assembly.transition;

import com.sfdev.assembly.callbacks.CallbackBase;

@FunctionalInterface
public interface TransitionCondition {
    boolean shouldTransition();
}
