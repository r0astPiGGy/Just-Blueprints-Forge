package com.rodev.jmcparser.patcher;

import org.jetbrains.annotations.Nullable;

public interface Patcher<T> {

    @Nullable
    T patch(T data);

    default boolean shouldPatch(T data) {
        return true;
    }

}
