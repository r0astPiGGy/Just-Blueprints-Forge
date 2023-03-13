package com.rodev.jmcparser.patcher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;

public abstract class Patch {

    @Nullable
    public Boolean remove;


    @JsonIgnore
    abstract public String getPatchedId();

    @JsonIgnore
    public boolean isRemoveType() {
        return remove != null && remove;
    }

}
