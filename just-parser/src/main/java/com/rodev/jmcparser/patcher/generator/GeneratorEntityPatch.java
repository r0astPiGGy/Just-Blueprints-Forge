package com.rodev.jmcparser.patcher.generator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rodev.jmcparser.patcher.Patch;
import org.jetbrains.annotations.Nullable;

public class GeneratorEntityPatch extends Patch {

    public String id;
    public Object schema;

    @Nullable
    public String type;

    @Override
    @JsonIgnore
    public String getPatchedId() {
        return id;
    }
}
