package com.rodev.jbpcore.blueprint.pin.exec_pin;

import com.rodev.jbpcore.blueprint.data.action.PinType;
import com.rodev.jbpcore.blueprint.pin.OutputPin;
import com.rodev.jbpcore.blueprint.pin.Pin;

public class OutExecPin extends OutputPin implements ExecPin {

    public OutExecPin(String name) {
        this(PinType.execType(name));
    }

    public OutExecPin(PinType pinType) {
        super(pinType);
    }

    @Override
    public boolean supportMultipleConnections() {
        return false;
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(!(another instanceof ExecPin)) return false;

        if(another instanceof OutExecPin) return false;

        return another.isInput();
    }
}
