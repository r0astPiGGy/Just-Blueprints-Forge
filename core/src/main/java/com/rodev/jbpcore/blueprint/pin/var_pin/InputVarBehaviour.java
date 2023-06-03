package com.rodev.jbpcore.blueprint.pin.var_pin;

import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.behaviour.InputBehaviour;
import com.rodev.jbpcore.blueprint.pin.dynamic.Dynamic;

public class InputVarBehaviour extends InputBehaviour {
    @Override
    public boolean isApplicable(Pin target, Pin another) {
        var anotherBehaviour = another.getConnectionBehaviour();

        if(another instanceof Dynamic dynamic) {
            if(!dynamic.isDynamicVariableSet()) {
                return anotherBehaviour.isOutput();
            }
        }

        if(!(another instanceof VarPin)) return false;

        if(anotherBehaviour instanceof InputVarBehaviour) return false;

        return target.isTheSameTypeAs(another) && anotherBehaviour.isOutput();
    }
}
