package com.rodev.jbpcore.blueprint.pin.var_pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.pin.*;

public interface VarPin extends Pin {

    static Pin outputPin(PinType pinType) {
        return new OutVarPin(pinType);
    }

    static Pin inputPin(PinType pinType) {
        return new InVarPin(pinType);
    }
}
