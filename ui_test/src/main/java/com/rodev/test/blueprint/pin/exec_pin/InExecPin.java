package com.rodev.test.blueprint.pin.exec_pin;

import com.rodev.test.blueprint.data.variable.VariableType;
import com.rodev.test.blueprint.pin.InputPin;
import com.rodev.test.blueprint.pin.Pin;
import com.rodev.test.blueprint.pin.var_pin.VarPin;

public class InExecPin extends InputPin implements ExecPin {

    public InExecPin() {
        super(VariableType.execType());
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(!(another instanceof ExecPin)) return false;

        if(another instanceof InExecPin) return false;

        return another.isOutput();
    }

}
