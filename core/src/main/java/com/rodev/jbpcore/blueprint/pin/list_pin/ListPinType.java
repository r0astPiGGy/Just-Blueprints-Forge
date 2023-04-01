package com.rodev.jbpcore.blueprint.pin.list_pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.list_pin.dynamic.DynamicInListPin;
import com.rodev.jbpcore.blueprint.pin.list_pin.dynamic.DynamicOutListPin;

public class ListPinType extends PinType {

    private final VariableType elementType;
    private final boolean dynamic;

    public ListPinType(String id, String name, VariableType type, VariableType elementType) {
        this(id, name, type, elementType, elementType.isDynamic());
    }

    public ListPinType(String id, String name, VariableType type, VariableType elementType, boolean dynamic) {
        super(id, name, type);

        this.elementType = elementType;
        this.dynamic = dynamic;
    }

    @Override
    public Pin createOutputPin() {
        if(dynamic) {
            return new DynamicOutListPin(this, elementType);
        }

        return new OutListPin(this, elementType);
    }

    @Override
    public Pin createInputPin() {
        if(dynamic) {
            return new DynamicInListPin(this, elementType);
        }

        return new InListPin(this, elementType);
    }
}
