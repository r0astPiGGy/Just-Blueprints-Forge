package com.rodev.test.blueprint.pin.var_pin;

import com.rodev.test.blueprint.pin.InputPin;
import com.rodev.test.blueprint.pin.Pin;

public class InVarPin extends InputPin implements VarPin {

    public InVarPin(int color) {
        super(color);
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(!(another instanceof VarPin)) return false;

        if(another instanceof InVarPin) return false;

        return getColor() == another.getColor() && another.isOutput();
    }

}
