package com.sfdev.assembly.transition;

import com.sfdev.assembly.transition.TransitionCondition;

/**
 * Timed transition class for simple usage of time based transitions
 */
public class TransitionTimed implements TransitionCondition {
    private double startTime = 0;
    private double time;
    //    private ElapsedTime timer = new ElapsedTime();
    public TransitionTimed(double time) {
        this.time = time;
    }

    public void startTimer() {
        startTime = System.nanoTime();
//        timer.reset();
    }

    public boolean timerStarted() {
        return startTime != 0;
//        return timer.seconds() != 0;
    }

    public void resetTimer() {
        startTime = 0;
    }

    @Override
    public boolean shouldTransition() {
        return (System.nanoTime() - startTime) / 1e9 > time;
    }
    /*public boolean shouldTransition() {
        return timer.seconds() > time;
    }*/

}
