package com.rodev.jbpcore.blueprint.pin.exec_pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.pin.InputPin;
import com.rodev.jbpcore.blueprint.pin.Pin;

public class InExecPin extends InputPin implements ExecPin {

    public InExecPin(PinType pinType) {
        super(pinType);
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(!(another instanceof ExecPin)) return false;

        if(another instanceof InExecPin) return false;

        return another.isOutput();
    }

}
