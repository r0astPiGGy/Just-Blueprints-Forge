package com.rodev.jbpcore.blueprint.pin.list_pin;

import com.rodev.jbpcore.data.action.pin_type.PinType;
import com.rodev.jbpcore.data.variable.VariableType;
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
    public Pin createInputPin() {
        return ListPin.inputPin(this);
    }

    @Override
    public Pin createOutputPin() {
        return ListPin.outputPin(this);
    }
}
