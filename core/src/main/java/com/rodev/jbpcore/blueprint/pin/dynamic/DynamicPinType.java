package com.rodev.jbpcore.blueprint.pin.dynamic;

import com.rodev.jbpcore.data.action.pin_type.PinType;
import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.var_pin.dynamic.DynamicVarPin;

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
        return DynamicVarPin.inputPin(this);
    }

    @Override
    public Pin createOutputPin() {
        return DynamicVarPin.inputPin(this);
    }

    public DynamicPinDestination getDependValueDestination() {
        return dependValueDestination;
    }

    public String getDependsOn() {
        return dependsOn;
    }
}
