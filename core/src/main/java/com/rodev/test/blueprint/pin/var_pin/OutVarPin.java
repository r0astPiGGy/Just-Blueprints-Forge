package com.rodev.test.blueprint.pin.var_pin;

import com.rodev.test.blueprint.data.action.PinType;
import com.rodev.test.blueprint.data.variable.VariableType;
import com.rodev.test.blueprint.pin.OutputPin;
import com.rodev.test.blueprint.pin.Pin;

import java.util.UUID;

public class OutVarPin extends OutputPin implements VarPin {

    public OutVarPin(PinType pinType) {
        super(pinType);
    }

    public OutVarPin(PinType pinType, UUID uuid) {
        super(pinType, uuid);
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(!(another instanceof VarPin)) return false;

        if(another instanceof OutVarPin) return false;

        return isTheSameTypeAs(another) && another.isInput();
    }
}
