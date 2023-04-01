package com.rodev.jbpcore.blueprint.pin.var_pin.dynamic;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.var_pin.InVarPin;

public class DynamicInVarPin extends InVarPin {
    public DynamicInVarPin(PinType pinType) {
        super(pinType);
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(isDynamicVariableSet()) {
            return super.isApplicable(another);
        }

        return another.isOutput();
    }
}
