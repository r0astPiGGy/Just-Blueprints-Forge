package com.rodev.jbpcore.blueprint.pin.list_pin.dynamic;

import com.google.errorprone.annotations.Var;
import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.dynamic.Dynamic;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicBehaviour;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicGroupResolver;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicPinDestination;
import com.rodev.jbpcore.blueprint.pin.list_pin.InListPin;
import com.rodev.jbpcore.blueprint.pin.list_pin.ListPin;
import com.rodev.jbpcore.blueprint.pin.list_pin.ListPinType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DynamicInListPin extends InListPin implements DynamicListPin {

    private final DynamicListPinType pinType;
    private final VariableType defaultType;
    private VariableType dynamic;

    public DynamicInListPin(DynamicListPinType pinType) {
        super(pinType, null);

        this.pinType = pinType;

        dynamic = defaultType = pinType.getElementType();
    }

    @Override
    public void setElementType(VariableType elementType) {
        dynamic = elementType;
    }

    @Override
    public DynamicListPinType getType() {
        return pinType;
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

    @Override
    public boolean isApplicable(Pin another) {
        if(isDynamicVariableSet()) {
            return super.isApplicable(another);
        }

        if(another.isInput()) return false;

        return another instanceof ListPin;
    }
}
