package com.sfdev.assembly.state;

/**
 * made cuz it can't rely on other imports
 * @param <f> The first parameter that is specified in the pair.
 * @param <s> The second parameter that is specified in the pair.
 */
public class Pair<f, s>{
     f first;
     s second;

    public Pair(f first, s second) {
        this.first = first;
        this.second = second;
    }
}
