package com.rodev.jbpcore.blueprint.pin.var_pin;

import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.behaviour.OutputBehaviour;
import com.rodev.jbpcore.blueprint.pin.dynamic.Dynamic;

public class OutputVarBehaviour extends OutputBehaviour {
    @Override
    public boolean isApplicable(Pin target, Pin another) {
        var anotherBehaviour = another.getConnectionBehaviour();
        
        if(another instanceof Dynamic dynamic) {
            if(!dynamic.isDynamicVariableSet()) {
                return anotherBehaviour.isInput();
            }
        }

        if(!(another instanceof VarPin)) return false;

        if(anotherBehaviour instanceof OutputVarBehaviour) return false;

        return target.isTheSameTypeAs(another) && anotherBehaviour.isInput();
    }
}
