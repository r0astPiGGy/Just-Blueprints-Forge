package com.rodev.jmcparser.data.action.alternate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;

public class AlternateAction {
    public String id;
    public String category;
    public String subcategory;

    public Object type;

    public Argument[] args;

    @JsonIgnore
    public Object icon;

    @JsonIgnore
    public String additionalInfo;

    @JsonIgnore
    public String worksWith;

    @JsonIgnore
    @Nullable
    public Argument getArgumentById(String id) {
        for (var arg : args) {
            if(arg.id.equals(id)) return arg;
        }

        return null;
    }

    public static class Argument {
        public String id;
        public boolean plural;
        public String type;

        @Nullable
        public String elementType;

        @Nullable
        public String keyType;

        @Nullable
        public String valueType;

        @JsonIgnore
        public Object valueSlots;
        @JsonIgnore
        public Object descriptionSlots;
        @JsonIgnore
        public Object values;
        @JsonIgnore
        public Object size;
        @JsonIgnore
        public Object defaultValue;
    }
}
