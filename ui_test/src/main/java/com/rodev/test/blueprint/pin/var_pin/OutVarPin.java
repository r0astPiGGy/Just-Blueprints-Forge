package com.rodev.test.blueprint.pin.var_pin;

import com.rodev.test.blueprint.pin.OutputPin;
import com.rodev.test.blueprint.pin.Pin;

public class OutVarPin extends OutputPin implements VarPin {

    public OutVarPin(int color) {
        super(color);
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(!(another instanceof VarPin)) return false;

        if(another instanceof OutVarPin) return false;

        return getColor() == another.getColor() && another.isInput();
    }
}
