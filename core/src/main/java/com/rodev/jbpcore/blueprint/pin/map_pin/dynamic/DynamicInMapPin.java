package com.rodev.jbpcore.blueprint.pin.map_pin.dynamic;

import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicBehaviour;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicGroupResolver;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicPinDestination;
import com.rodev.jbpcore.blueprint.pin.map_pin.InMapPin;
import com.rodev.jbpcore.blueprint.pin.map_pin.MapPin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DynamicInMapPin extends InMapPin implements DynamicMapPin {

    private final DynamicMapPinType pinType;
    private final VariableType defaultKeyType;
    private final VariableType defaultValueType;
    private VariableType dynamicKeyType;
    private VariableType valueType;

    public DynamicInMapPin(DynamicMapPinType pinType) {
        super(pinType, null, null);
        this.dynamicKeyType = defaultKeyType = pinType.getKeyType();
        this.valueType = defaultValueType = pinType.getValueType();
        this.pinType = pinType;
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(isDynamicVariableSet()) {
            return super.isApplicable(another);
        }

        if(another.isInput()) return false;

        return another instanceof MapPin;
    }

    @Override
    public boolean isDynamicVariableSet() {
        return isDynamicKeySet() && isDynamicValueSet();
    }

    public boolean isDynamicKeySet() {
        return dynamicKeyType != defaultKeyType;
    }

    public boolean isDynamicValueSet() {
        return valueType != defaultValueType;
    }

    @Override
    public DynamicMapPinType getType() {
        return pinType;
    }

    @Override
    public Map<DynamicPinDestination, VariableType> getAffectedDestinationsFromConnection(Pin connection) {
        if(!(connection instanceof MapPin mapPin)) {
            throw new IllegalArgumentException("Should be a map");
        }

        var map = new HashMap<DynamicPinDestination, VariableType>();

        boolean keyDynamic = defaultKeyType.isDynamic();
        boolean valueDynamic = defaultValueType.isDynamic();

        if(isDynamicRoot()) {
            if (keyDynamic) {
                map.put(DynamicPinDestination.MAP_KEY, mapPin.getKeyType());
            }
            if (valueDynamic) {
                map.put(DynamicPinDestination.MAP_VALUE, mapPin.getValueType());
            }
        } else {
            if(keyDynamic) {
                map.put(pinType.getKeyDestination(), mapPin.getKeyType());
            }
            if(valueDynamic) {
                map.put(pinType.getValueDestination(), mapPin.getValueType());
            }
        }

        return map;
    }

    @Override
    public boolean isDynamicRoot() {
        return pinType.getKeyDependsOn() == null && pinType.getValueDependsOn() == null;
    }

    @Override
    public void onAddToGroupDelegate(DynamicGroupResolver resolver) {
        if(isDynamicRoot()) {
            throw new IllegalStateException("Couldn't get here.");
        }

        var valueDependsOn = pinType.getValueDependsOn();
        var valueDestination = pinType.getValueDestination();
        var keyDependsOn = pinType.getKeyDependsOn();
        var keyDestination = pinType.getKeyDestination();

        if(valueDependsOn != null) {
            Objects.requireNonNull(valueDestination);
            resolver.addPinToGroup(valueDependsOn, this, DynamicBehaviour.of(
                    valueDestination,
                    this::setValueType,
                    this::resetValueType
            ));
        }

        if(keyDependsOn != null) {
            Objects.requireNonNull(keyDestination);
            resolver.addPinToGroup(keyDependsOn, this, DynamicBehaviour.of(
                    keyDestination,
                    this::setKeyType,
                    this::resetKeyType
            ));
        }
    }

    @Override
    public void setKeyType(VariableType variableType) {
        dynamicKeyType = variableType;
    }

    @Override
    public void resetKeyType() {
        dynamicKeyType = defaultKeyType;
    }

    @Override
    public void setValueType(VariableType variableType) {
        valueType = variableType;
    }

    @Override
    public void resetValueType() {
        valueType = defaultValueType;
    }

    @Override
    public VariableType getKeyType() {
        return dynamicKeyType;
    }

    @Override
    public VariableType getValueType() {
        return valueType;
    }
}
