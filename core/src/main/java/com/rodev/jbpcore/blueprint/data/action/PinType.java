package com.rodev.jbpcore.blueprint.data.action;

import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.exec_pin.ExecPin;
import com.rodev.jbpcore.blueprint.pin.var_pin.VarPin;

import java.util.Objects;

public class PinType {
    private final String id;
    private final String name;
    private final VariableType type;

    public static PinType execType(String name) {
        return execType("exec", name);
    }

    public static PinType execType(String id, String name) {
        return new PinType(id, name, VariableType.execType()) {
            @Override
            public Pin createInputPin() {
                return ExecPin.inputPin(this);
            }

            @Override
            public Pin createOutputPin() {
                return ExecPin.outputPin(this);
            }

            @Override
            public String getType() {
                return "exec";
            }
        };
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
}
