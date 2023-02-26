package com.rodev.jmcparser.json;

import org.jetbrains.annotations.Nullable;

public class ActionData {
    public String id;

    public String name;

    public String object;

    public ActionDataArgument[] args;

    @Nullable
    public String origin;

    @Nullable
    public String assigning;

    @Nullable
    public String containing;

    @Nullable
    public String[] lambda;

    @Nullable
    public Boolean conditional;
}
