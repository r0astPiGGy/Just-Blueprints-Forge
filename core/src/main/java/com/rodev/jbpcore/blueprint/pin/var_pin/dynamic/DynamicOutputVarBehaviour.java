package com.rodev.jbpcore.blueprint.pin.var_pin.dynamic;

import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.dynamic.Dynamic;
import com.rodev.jbpcore.blueprint.pin.var_pin.InputVarBehaviour;

public class DynamicOutputVarBehaviour extends InputVarBehaviour {

    @Override
    public boolean isApplicable(Pin target, Pin another) {
        var dynamic = (Dynamic) target;

        if(dynamic.isDynamicVariableSet()) {
            return super.isApplicable(target, another);
        }

        return another.getConnectionBehaviour().isInput();
    }
}
