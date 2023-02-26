package com.rodev.jmcparser.json;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.jetbrains.annotations.Nullable;

public class ActionDataArgument {
    public String type;
    public String name;

    @Nullable
    public Boolean array;

    @Nullable
    public Integer length;

    @Nullable
    @JsonAlias("enum")
    public String[] _enum;

    @Nullable
    public Boolean defaultBooleanValue;
}
