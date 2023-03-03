package com.rodev.jmcparser.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;

public class GameValue {

    public String id;
    public String type;

    public String name;

    @Nullable
    public String[] worksWith;

    @Nullable
    public String keyType;

    @Nullable
    public String valueType;

    @Nullable
    public String elementType;

    @JsonIgnore
    public Object icon;

}
