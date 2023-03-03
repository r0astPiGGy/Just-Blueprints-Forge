package com.rodev.jmcparser.json;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.LinkedList;
import java.util.List;

public class Event {

    public String id;

    @JsonIgnore
    public String[] worksWith;

    public boolean cancellable;
    public String category;

    public final List<GameValue> rawOutput = new LinkedList<>();

}
