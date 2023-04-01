package com.rodev.jbpcore.blueprint.data.action.pin_type;

import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.exec_pin.ExecPin;
import com.rodev.jbpcore.blueprint.pin.var_pin.VarPin;

import java.util.Objects;

public class PinType implements Cloneable {
    private final String id;
    private final String name;
    private final VariableType type;

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

    public String getType() {
        return "default";
    }

    public VariableType getVariableType() {
        return type;
    }

    public Pin createInputPin() {
        return VarPin.inputPin(this);
    }

    public Pin createOutputPin() {
        return VarPin.outputPin(this);
    }

    public boolean isDynamic() {
        return false;
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
                "outputPin=" + id + ", " +
                "name=" + name + ", " +
                "type=" + type + ']';
    }

    @Override
    public PinType clone() {
        try {
            return (PinType) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
