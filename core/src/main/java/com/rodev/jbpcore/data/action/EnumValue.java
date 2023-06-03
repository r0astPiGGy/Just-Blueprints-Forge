package com.rodev.jbpcore.data.action;

public record EnumValue(String key, String label) {

    @Override
    public String toString() {
        return label;
    }
}
