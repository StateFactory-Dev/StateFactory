package com.sfdev.examples;

import com.sfdev.assembly.state.*;

public class FSMSim {

    public static double startTime;
    enum States {
        FIRST,
        SECOND,
        THIRD
    }

    public static int a = 0;

    public static void main(String[] args) {

        Enum state = States.FIRST;

            StateMachine machine = new StateMachineBuilder()
                .state(States.FIRST)
                .onEnter(() -> { System.out.println("I have entered the first state"); a += 1; System.out.println(a);})
                .onExit(() -> { System.out.println("I am exiting the first state"); a += 1; System.out.println(a);})
                .transition(() -> a > 0, States.SECOND) // first state transition
                .state(States.SECOND)
                .onEnter(() ->{ System.out.println("I have entered the second state"); a += 1; System.out.println(a);})
                .onExit(() -> { System.out.println("I have exited the second state"); a+=1; System.out.println(a);})
                .transition(() -> a > 2, States.THIRD) // seconds state transition
                .state(States.THIRD)
                .onEnter(() -> { System.out.println("Entered the third state"); a+=1; System.out.println(a);})
                .build();

        machine.start();
        while(a < 4) {
            machine.update();
        }
        System.out.println("prog is done");
    }
}