package com.sfdev.assembly.transition;

@FunctionalInterface
public interface TransitionCondition {
    boolean shouldTransition();
}
