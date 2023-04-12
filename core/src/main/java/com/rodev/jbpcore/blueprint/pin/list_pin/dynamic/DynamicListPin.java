package com.rodev.jbpcore.blueprint.pin.list_pin.dynamic;

import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.dynamic.Dynamic;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicBehaviour;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicGroup;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicPinDestination;
import com.rodev.jbpcore.blueprint.pin.dynamic.impl.DynamicGroupImpl;
import com.rodev.jbpcore.blueprint.pin.list_pin.ListPin;

public interface DynamicListPin extends ListPin, Dynamic {

    @Override
    default DynamicGroup createDynamicGroup() {
        return DynamicGroup.of(this, DynamicBehaviour.of(
                DynamicPinDestination.LIST_ELEMENT,
                this::setElementType,
                this::resetVariableType
        ));
    }

    void setElementType(VariableType elementType);

}
