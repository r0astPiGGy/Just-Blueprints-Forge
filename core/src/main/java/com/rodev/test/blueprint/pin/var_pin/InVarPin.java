package com.rodev.test.blueprint.pin.var_pin;

import com.rodev.test.blueprint.data.action.PinType;
import com.rodev.test.blueprint.data.variable.VariableType;
import com.rodev.test.blueprint.pin.InputPin;
import com.rodev.test.blueprint.pin.Pin;

import java.util.UUID;

public class InVarPin extends InputPin implements VarPin {

    public InVarPin(PinType pinType) {
        super(pinType);
    }

    public InVarPin(PinType pinType, UUID uuid) {
        super(pinType, uuid);
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(!(another instanceof VarPin)) return false;

        if(another instanceof InVarPin) return false;

        return isTheSameTypeAs(another) && another.isOutput();
    }

}
