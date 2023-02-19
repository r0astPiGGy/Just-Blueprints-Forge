package com.rodev.test.blueprint.pin.var_pin;

import com.rodev.test.blueprint.data.variable.VariableType;
import com.rodev.test.blueprint.pin.OutputPin;
import com.rodev.test.blueprint.pin.Pin;

import java.util.UUID;

public class OutVarPin extends OutputPin implements VarPin {

    public OutVarPin(VariableType variableType) {
        super(variableType);
    }

    public OutVarPin(VariableType variableType, UUID uuid) {
        super(variableType, uuid);
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(!(another instanceof VarPin)) return false;

        if(another instanceof OutVarPin) return false;

        return isTheSameTypeAs(another) && another.isInput();
    }
}
