package com.rodev.jbpcore.blueprint.pin.var_pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.pin.OutputPin;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.dynamic.Dynamic;

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
        if(another instanceof Dynamic dynamic) {
            if(!dynamic.isDynamicVariableSet()) {
                return another.isInput();
            }
        }

        if(!(another instanceof VarPin)) return false;

        if(another instanceof OutVarPin) return false;

        return isTheSameTypeAs(another) && another.isInput();
    }
}
