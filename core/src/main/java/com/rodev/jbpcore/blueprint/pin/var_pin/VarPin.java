package com.rodev.jbpcore.blueprint.pin.var_pin;

import com.rodev.jbpcore.blueprint.pin.behaviour.ConnectionBehaviour;
import com.rodev.jbpcore.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.pin.*;

public class VarPin extends PinImpl {

    protected VarPin(PinType pinType, ConnectionBehaviour connectionBehaviour) {
        super(pinType, connectionBehaviour);
    }

    public static VarPin outputPin(PinType pinType) {
        return new VarPin(pinType, new OutputVarBehaviour());
    }

    public static VarPin inputPin(PinType pinType) {
        return new VarPin(pinType, new InputVarBehaviour());
    }
}
