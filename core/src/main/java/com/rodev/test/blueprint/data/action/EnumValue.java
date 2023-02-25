package com.rodev.test.blueprint.data.action;

public record EnumValue(String key, String label) {

    @Override
    public String toString() {
        return label;
    }
}
