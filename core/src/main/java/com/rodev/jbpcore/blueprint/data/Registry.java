package com.rodev.jbpcore.blueprint.data;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Registry<ID, T> {

    protected final Map<ID, T> data = new HashMap<>();

    @Nullable
    public T get(ID id) {
        return data.get(id);
    }

}
