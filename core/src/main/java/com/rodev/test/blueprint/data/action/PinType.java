package com.rodev.test.blueprint.data.action;

import com.rodev.test.blueprint.data.variable.VariableType;

import java.util.Objects;

public class PinType {
    private final String id;
    private final String name;
    private final VariableType type;

    public static PinType execType(String name) {
        return execType("exec", name);
    }

    public static PinType execType(String id, String name) {
        return new PinType(id, name, VariableType.execType());
    }

    public PinType(String id, String name, VariableType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public VariableType getVariableType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (PinType) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type);
    }

    @Override
    public String toString() {
        return "PinType[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "type=" + type + ']';
    }
}
