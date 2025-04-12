package com.sfdev.assembly.callbacks;

/**
 * TimedCallback class to execute actions after a specified amount of time
 */
public class TimedCallback implements CallbackBase{
    private double startTime = 0;
    private double time;
    private CallbackBase callback;
    private boolean done = false;
    public TimedCallback(double time, CallbackBase callback) {
        this.time = time;
        this.callback = callback;
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public boolean timerStarted() {
        return startTime != 0;
    }

    public void resetTimer() {
        done = false;
        startTime = 0;
    }
    public double getTime() {
        return time;
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public void call() {
        if (!done && (System.nanoTime() - startTime) / 1e9 > time) {
            callback.call();
            done = true;
        }
    }

    @Override
    public String toString() {
        return Double.toString(time);
    }
}
