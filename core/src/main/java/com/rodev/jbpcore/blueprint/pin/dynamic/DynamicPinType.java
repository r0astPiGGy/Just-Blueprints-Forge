package com.rodev.jbpcore.blueprint.pin.dynamic;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.var_pin.dynamic.DynamicInVarPin;
import com.rodev.jbpcore.blueprint.pin.var_pin.dynamic.DynamicOutVarPin;

public class DynamicPinType extends PinType {

    private final String dependsOn;
    private final DynamicPinDestination dependValueDestination;

    public DynamicPinType(String id, String name, VariableType type, DynamicDependency valueType) {
        super(id, name, type);

        if(valueType == null) {
            dependsOn = null;
            dependValueDestination = null;
        } else {
            dependsOn = valueType.dependsOn();
            dependValueDestination = valueType.getDestination();
        }
    }

    @Override
    public Pin createInputPin() {
        return new DynamicInVarPin(this);
    }

    @Override
    public Pin createOutputPin() {
        return new DynamicOutVarPin(this);
    }

    public DynamicPinDestination getDependValueDestination() {
        return dependValueDestination;
    }

    public String getDependsOn() {
        return dependsOn;
    }
}
