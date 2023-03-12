package com.rodev.jbpcore.blueprint.pin.var_pin;

import com.rodev.jbpcore.blueprint.data.action.PinType;
import com.rodev.jbpcore.blueprint.pin.InputPin;
import com.rodev.jbpcore.blueprint.pin.Pin;

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
