package com.rodev.jbpcore.utils;

import java.util.function.Function;

public class StringParse {

    public static <T> void parseAll(String[] array, T[] arrayTo, Function<String, T> parser) {
        for(int i = 0; i < array.length; i++) {
            arrayTo[i] = parser.apply(array[i]);
        }
    }

}
