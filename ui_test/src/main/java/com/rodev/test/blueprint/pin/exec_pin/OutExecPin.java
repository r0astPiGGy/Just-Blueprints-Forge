package com.rodev.test.blueprint.pin.exec_pin;

import com.rodev.test.blueprint.pin.OutputPin;
import com.rodev.test.blueprint.pin.Pin;
import com.rodev.test.blueprint.pin.var_pin.VarPin;

public class OutExecPin extends OutputPin implements ExecPin {

    public OutExecPin(int color) {
        super(color);
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
