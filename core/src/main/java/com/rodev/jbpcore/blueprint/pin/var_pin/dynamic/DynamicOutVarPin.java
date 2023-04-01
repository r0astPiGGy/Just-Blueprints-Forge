package com.rodev.jbpcore.blueprint.pin.var_pin.dynamic;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.var_pin.OutVarPin;

import java.util.UUID;

public class DynamicOutVarPin extends OutVarPin {
    public DynamicOutVarPin(PinType pinType) {
        super(pinType);
    }

    public DynamicOutVarPin(PinType pinType, UUID uuid) {
        super(pinType, uuid);
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(isDynamicVariableSet()) {
            return super.isApplicable(another);
        }

        return another.isInput();
    }

}
