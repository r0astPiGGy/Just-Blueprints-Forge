package com.rodev.jmcparser.patcher.variable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rodev.jmcparser.patcher.Patch;
import com.rodev.jmcparser.patcher.Patchable;

public class VariableTypePatch extends Patch {

    public String id;

    @Patchable
    public String color;

    @Patchable
    public String icon;

    @Override
    @JsonIgnore
    public String getPatchedId() {
        return id;
    }
}
