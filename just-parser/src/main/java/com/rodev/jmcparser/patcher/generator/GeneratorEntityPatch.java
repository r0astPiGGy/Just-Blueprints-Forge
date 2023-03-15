package com.rodev.jmcparser.patcher.generator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rodev.jmcparser.patcher.Patch;
import com.rodev.jmcparser.patcher.Patchable;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class GeneratorEntityPatch extends Patch {

    public String id;

    @Patchable
    public Object schema;

    @Nullable
    @Patchable
    public Set<String> ignoreArguments;

    @Nullable
    @Patchable
    public String type;

    @Nullable
    @Patchable
    public Map<String, String> output;

    @Override
    @JsonIgnore
    public String getPatchedId() {
        return id;
    }
}
