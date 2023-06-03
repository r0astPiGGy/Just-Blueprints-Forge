package com.rodev.jbpcore.data.json;

import com.fasterxml.jackson.annotation.JsonAlias;

public class PinIconEntity {
    public String id;
    public String icon;
    @JsonAlias("connected-icon")
    public String connectedIcon;
}
