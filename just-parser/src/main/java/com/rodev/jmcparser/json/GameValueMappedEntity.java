package com.rodev.jmcparser.json;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;

public class GameValueMappedEntity {

    @JsonAlias("id")
    public String mappedId;

    @JsonAlias("name")
    public String realId;

    public String type;

    @JsonIgnore
    @Nullable
    public String map;

    @JsonIgnore
    @Nullable
    public String array;

}
