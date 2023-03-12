package com.rodev.jbpcore.blueprint.data.selectors;

public record Selector(String id, String name) {

    @Override
    public String toString() {
        return name;
    }
}
