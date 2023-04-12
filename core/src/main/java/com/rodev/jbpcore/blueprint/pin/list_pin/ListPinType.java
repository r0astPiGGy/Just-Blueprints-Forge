package com.rodev.jbpcore.blueprint.pin.list_pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;

public class ListPinType extends PinType {

    private final VariableType elementType;

    public ListPinType(String id, String name, VariableType type, VariableType elementType) {
        super(id, name, type);

        this.elementType = elementType;
    }

    public VariableType getElementType() {
        return elementType;
    }

    @Override
    public Pin createOutputPin() {
        return new OutListPin(this, elementType);
    }

    @Override
    public Pin createInputPin() {
        return new InListPin(this, elementType);
    }
}
