package com.rodev.jbpcore.blueprint.pin.list_pin.dynamic;

import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicDependency;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicPinDestination;
import com.rodev.jbpcore.blueprint.pin.list_pin.ListPinType;
import org.jetbrains.annotations.Nullable;

public class DynamicListPinType extends ListPinType {

    private final DynamicPinDestination dynamicPinDestination;
    private final String dependsOn;

    public DynamicListPinType(
            String id,
            String name,
            VariableType type,
            VariableType elementType,
            @Nullable DynamicDependency elementDependency
    ) {
        super(id, name, type, elementType);

        if(elementDependency == null) {
            dynamicPinDestination = null;
            dependsOn = null;
        } else {
            dynamicPinDestination = elementDependency.getDestination();
            dependsOn = elementDependency.dependsOn();
        }
    }

    public DynamicPinDestination getDynamicPinDestination() {
        return dynamicPinDestination;
    }

    public String getDependsOn() {
        return dependsOn;
    }

    @Override
    public Pin createInputPin() {
        return DynamicListPin.inputPin(this);
    }

    @Override
    public Pin createOutputPin() {
        return DynamicListPin.outputPin(this);
    }
}
