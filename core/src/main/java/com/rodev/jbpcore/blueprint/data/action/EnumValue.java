package com.rodev.jbpcore.blueprint.data.action;

public record EnumValue(String key, String label) {

    @Override
    public String toString() {
        return label;
    }
}
