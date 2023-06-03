package com.rodev.jbpcore.blueprint.pin.map_pin;

import com.rodev.jbpcore.data.action.pin_type.PinType;
import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;

public class MapPinType extends PinType {

    private final VariableType keyType;
    private final VariableType valueType;

    public MapPinType(String id, String name, VariableType type, VariableType keyType, VariableType valueType) {
        super(id, name, type);
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public VariableType getKeyType() {
        return keyType;
    }

    public VariableType getValueType() {
        return valueType;
    }

    @Override
    public Pin createInputPin() {
        return MapPin.inputPin(this);
    }

    @Override
    public Pin createOutputPin() {
        return MapPin.outputPin(this);
    }
}
