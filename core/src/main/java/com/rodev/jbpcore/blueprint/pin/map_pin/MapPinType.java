package com.rodev.jbpcore.blueprint.pin.map_pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.map_pin.dynamic.DynamicInMapPin;
import com.rodev.jbpcore.blueprint.pin.map_pin.dynamic.DynamicOutMapPin;

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
        return new InMapPin(this, keyType, valueType);
    }

    @Override
    public Pin createOutputPin() {
        return new OutMapPin(this, keyType, valueType);
    }
}
