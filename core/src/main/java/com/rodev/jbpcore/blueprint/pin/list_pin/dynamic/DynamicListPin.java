package com.rodev.jbpcore.blueprint.pin.list_pin.dynamic;

import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.behaviour.ConnectionBehaviour;
import com.rodev.jbpcore.blueprint.pin.dynamic.*;
import com.rodev.jbpcore.blueprint.pin.var_pin.dynamic.DynamicOutputVarBehaviour;
import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.list_pin.ListPin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DynamicListPin extends ListPin implements Dynamic {

    private final DynamicListPinType pinType;
    private final VariableType defaultType;
    private VariableType dynamic;

    protected DynamicListPin(DynamicListPinType pinType, ConnectionBehaviour connectionBehaviour) {
        super(pinType, connectionBehaviour);

        this.pinType = pinType;

        dynamic = defaultType = pinType.getElementType();
    }

    public static DynamicListPin inputPin(DynamicListPinType pinType) {
        return new DynamicListPin(pinType, new DynamicInputListBehaviour());
    }

    public static DynamicListPin outputPin(DynamicListPinType pinType) {
        return new DynamicListPin(pinType, new DynamicOutputListBehaviour());
    }

    @Override
    public DynamicGroup createDynamicGroup() {
        return DynamicGroup.of(this, DynamicBehaviour.of(
                DynamicPinDestination.LIST_ELEMENT,
                this::setElementType,
                this::resetVariableType
        ));
    }

    @Override
    public Map<DynamicPinDestination, VariableType> getAffectedDestinationsFromConnection(Pin connection) {
        if(!(connection instanceof ListPin listPin)) {
            throw new IllegalArgumentException("Should be a list.");
        }

        var map = new HashMap<DynamicPinDestination, VariableType>();

        if(isDynamicRoot()) {
            map.put(DynamicPinDestination.LIST_ELEMENT, listPin.getElementType());
        } else {
            map.put(pinType.getDynamicPinDestination(), listPin.getElementType());
        }

        return map;
    }

    public void setElementType(VariableType elementType) {
        dynamic = elementType;
    }

    @Override
    public DynamicListPinType getType() {
        return pinType;
    }

    @Override
    public boolean isDynamicRoot() {
        return pinType.getDependsOn() == null;
    }

    @Override
    public void onAddToGroupDelegate(DynamicGroupResolver resolver) {
        resolver.addPinToGroup(pinType.getDependsOn(), this, DynamicBehaviour.of(
                pinType.getDynamicPinDestination(),
                this::setElementType,
                this::resetVariableType
        ));
    }
    @Override
    public void setVariableType(@NotNull VariableType variableType) {
        throw new IllegalStateException("Not supported");
    }

    @Override
    public void resetVariableType() {
        dynamic = defaultType;
    }

    @Override
    public boolean isDynamicVariableSet() {
        return dynamic != defaultType;
    }

    @Override
    public @NotNull VariableType getElementType() {
        return dynamic;
    }

    public static DynamicListPin castToDynamicListOrNull(Pin pin) {
        return pin instanceof DynamicListPin ? (DynamicListPin) pin : null;
    }

    public static DynamicListPin castToDynamicList(Pin pin) {
        return (DynamicListPin) pin;
    }

}
