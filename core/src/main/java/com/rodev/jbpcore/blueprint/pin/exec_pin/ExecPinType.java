package com.rodev.jbpcore.blueprint.pin.exec_pin;

import com.rodev.jbpcore.data.action.pin_type.PinType;
import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;

public class ExecPinType extends PinType {

    public ExecPinType(String id, String name, VariableType type) {
        super(id, name, type);
    }

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
}
