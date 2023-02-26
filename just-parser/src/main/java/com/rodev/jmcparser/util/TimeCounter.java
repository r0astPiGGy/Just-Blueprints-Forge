package com.rodev.jmcparser.util;

import java.util.function.Function;

public class TimeCounter {

    private long millis;

    public TimeCounter() {
        reset();
    }

    public void reset() {
        millis = System.currentTimeMillis();
    }

    public void print(Function<Long, String> function) {
        var estimated = System.currentTimeMillis() - millis;
        System.out.println(function.apply(estimated));
    }

    public void printAndReset(Function<Long, String> function) {
        print(function);
        reset();
    }

}
