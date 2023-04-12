package com.rodev.jbpcore.blueprint.pin.map_pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.OutputPin;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.map_pin.dynamic.DynamicMapPin;
import com.rodev.jbpcore.blueprint.pin.var_pin.OutVarPin;
import com.rodev.jbpcore.blueprint.pin.var_pin.VarPin;

import java.util.UUID;

public class OutMapPin extends OutputPin implements MapPin {

    private final VariableType keyType;
    private final VariableType valueType;

    protected ColorChangeListener keyColorChangeListener;
    protected ColorChangeListener valueColorChangeListener;

    public OutMapPin(PinType pinType, VariableType keyType, VariableType valueType) {
        super(pinType);
        this.valueType = valueType;
        this.keyType = keyType;
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(!(another instanceof MapPin mapPin)) return false;

        if(mapPin instanceof DynamicMapPin dynamicMapPin) {
            if(!dynamicMapPin.isInput()) return false;

            if(dynamicMapPin.isDynamicKeySet()) {
                if(!dynamicMapPin.getKeyType().equals(getKeyType())) {
                    return false;
                }
            }
            if(dynamicMapPin.isDynamicValueSet()) {
                return dynamicMapPin.getValueType().equals(getValueType());
            }
            return true;
        }

        if(mapPin instanceof OutMapPin) return false;

        var anotherKeyType = mapPin.getKeyType();
        var anotherValueType = mapPin.getValueType();

        return getKeyType().equals(anotherKeyType) && getValueType().equals(anotherValueType) && another.isInput();
    }

    @Override
    public void onKeyTypeColorChangeListenerSet(ColorChangeListener listener) {
        keyColorChangeListener = listener;
        updateKeyColor();
    }

    @Override
    public void onValueTypeColorChangeListenerSet(ColorChangeListener listener) {
        valueColorChangeListener = listener;
        updateValueColor();
    }

    protected void updateKeyColor() {
        keyColorChangeListener.setColor(getKeyType().color());
    }

    protected void updateValueColor() {
        valueColorChangeListener.setColor(getValueType().color());
    }

    @Override
    public VariableType getKeyType() {
        return keyType;
    }

    @Override
    public VariableType getValueType() {
        return valueType;
    }

}
