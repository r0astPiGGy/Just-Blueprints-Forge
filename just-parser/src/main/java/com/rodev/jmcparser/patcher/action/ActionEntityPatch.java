package com.rodev.jmcparser.patcher.action;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rodev.jmcparser.patcher.Patch;
import com.rodev.jmcparser.patcher.Patchable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ActionEntityPatch extends Patch {

    public String id;

    @Patchable
    public String type;

    @Patchable
    public String name;

    @Patchable
    public String category;

    @Nullable
    public List<PinTypePatch> input;

    @Nullable
    public List<PinTypePatch> output;

    @Patchable
    public Object extra_data;

    @Patchable
    public String icon_namespace;

    @Override
    @JsonIgnore
    public String getPatchedId() {
        return id;
    }

    public static class PinTypePatch extends Patch {
        public String id;

        @Patchable
        public String label;

        @Patchable
        public String type;

        @Patchable
        public Object extra_data;

        @Override
        @JsonIgnore
        public String getPatchedId() {
            return id;
        }
    }
}
