package com.sfdev.assembly.state;

/**
 * A class that holds 3 objects.
 * @param <F> The type of the first object in the triple.
 * @param <S> The type of the second object in the triple.
 * @param <T> The type of the third object in the triple.
 */
public class Triple<F, S, T>{

    F first;
    S second;
    T third;

    /**
     * A class that holds 3 objects.
     * @param first The first object that needs to be stored in the triple.
     * @param second The second object that needs to be stored in the triple.
     * @param third The third object that needs to be stored in the triple.
     */
    public Triple(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    /**
     * Getter for the first object.
     * @return Returns the first object that was passed into the constructor.
     */
    public F getFirst() {
        return first;
    }

    /**
     * Getter for the second object.
     * @return Returns the second object that was passed into the constructor.
     */
    public S getSecond() {
        return second;
    }

    /**
     * Getter for the third object.
     * @return Returns the third object that was passed into the constructor.
     */
    public T getThird() {
        return third;
    }
}
