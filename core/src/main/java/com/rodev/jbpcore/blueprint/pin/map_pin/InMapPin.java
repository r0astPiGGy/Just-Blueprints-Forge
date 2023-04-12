package com.rodev.jbpcore.blueprint.pin.map_pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.InputPin;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicPinDestination;
import com.rodev.jbpcore.blueprint.pin.map_pin.dynamic.DynamicMapPin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InMapPin extends InputPin implements MapPin {

    private final VariableType keyType;
    private final VariableType valueType;

    protected ColorChangeListener keyColorChangeListener;
    protected ColorChangeListener valueColorChangeListener;

    public InMapPin(PinType pinType, VariableType keyType, VariableType valueType) {
        super(pinType);
        this.valueType = valueType;
        this.keyType = keyType;
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(!(another instanceof MapPin mapPin)) return false;

        if(mapPin instanceof DynamicMapPin dynamicMapPin) {
            if(!dynamicMapPin.isOutput()) return false;

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

        if(mapPin instanceof InMapPin) return false;

        var anotherKeyType = mapPin.getKeyType();
        var anotherValueType = mapPin.getValueType();

        return getKeyType().equals(anotherKeyType) && getValueType().equals(anotherValueType) && another.isOutput();
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
