package com.rodev.jmcparser.patcher.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rodev.jmcparser.patcher.Patch;
import com.rodev.jmcparser.patcher.Patchable;

public class CategoryPatch extends Patch {
    public String id;

    @Patchable
    public String name;

    @Override
    @JsonIgnore
    public String getPatchedId() {
        return id;
    }
}
