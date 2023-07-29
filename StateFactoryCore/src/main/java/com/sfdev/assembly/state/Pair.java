package com.sfdev.assembly.state;

/**
 * made cuz it can't rely on other imports
 * @param <f> The type of first the parameter that is specified in the pair.
 * @param <s> The type of the second parameter that is specified in the pair.
 */
public class Pair<f, s>{
     f first;
     s second;

    /**
     * Constructs the pair.
      * @param first The first object to be stored in the pair.
     * @param second The second object to be stored in the pair.
     */
    public Pair(f first, s second) {
        this.first = first;
        this.second = second;
    }
}
