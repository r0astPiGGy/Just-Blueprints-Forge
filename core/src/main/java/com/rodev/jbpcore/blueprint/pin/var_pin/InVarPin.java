package com.rodev.jbpcore.blueprint.pin.var_pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.pin.InputPin;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.dynamic.Dynamic;

import java.util.UUID;

public class InVarPin extends InputPin implements VarPin {

    public InVarPin(PinType pinType) {
        super(pinType);
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(another instanceof Dynamic dynamic) {
            if(!dynamic.isDynamicVariableSet()) {
                return another.isOutput();
            }
        }

        if(!(another instanceof VarPin)) return false;

        if(another instanceof InVarPin) return false;

        return isTheSameTypeAs(another) && another.isOutput();
    }

}
