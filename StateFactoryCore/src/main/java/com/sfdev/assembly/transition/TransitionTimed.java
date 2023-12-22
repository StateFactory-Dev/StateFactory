package com.sfdev.assembly.transition;

/**
 * Timed transition class for simple usage of time based transitions
 */
public class TransitionTimed implements TransitionCondition {
    private double startTime = 0;
    private double time;
    public TransitionTimed(double time) {
        this.time = time;
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public boolean timerStarted() {
        return startTime != 0;
    }

    public void resetTimer() {
        startTime = 0;
    }

    @Override
    public boolean shouldTransition() {
        return (System.nanoTime() - startTime) / 1e9 > time;
    }
}
