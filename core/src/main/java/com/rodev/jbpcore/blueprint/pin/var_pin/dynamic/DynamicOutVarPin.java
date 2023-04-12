package com.rodev.jbpcore.blueprint.pin.var_pin.dynamic;

import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.dynamic.*;
import com.rodev.jbpcore.blueprint.pin.map_pin.dynamic.DynamicMapPin;
import com.rodev.jbpcore.blueprint.pin.var_pin.OutVarPin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class DynamicOutVarPin extends OutVarPin implements Dynamic {
    private final VariableType defaultType;
    private final DynamicPinType pinType;
    private VariableType dynamic;

    public DynamicOutVarPin(DynamicPinType pinType) {
        super(pinType);

        this.pinType = pinType;

        dynamic = defaultType = pinType.getVariableType();
    }

    @Override
    public DynamicPinType getType() {
        return pinType;
    }

    @Override
    public Map<DynamicPinDestination, VariableType> getAffectedDestinationsFromConnection(Pin connection) {
        var map = new HashMap<DynamicPinDestination, VariableType>();

        if(isDynamicRoot()) {
            map.put(DynamicPinDestination.TYPE, connection.getVariableType());
        } else {
            map.put(pinType.getDependValueDestination(), connection.getVariableType());
        }

        return map;
    }

    @Override
    public boolean isDynamicRoot() {
        return getType().getDependsOn() == null;
    }

    @Override
    public void onAddToGroupDelegate(DynamicGroupResolver resolver) {
        if(isDynamicRoot())
            throw new IllegalStateException("Couldn't get here.");

        resolver.addPinToGroup(getType().getDependsOn(), this, DynamicBehaviour.of(
                getType().getDependValueDestination(),
                this::setVariableType,
                this::resetVariableType
        ));
    }

    @Override
    public void setVariableType(@NotNull VariableType variableType) {
        dynamic = variableType;
    }

    @Override
    public VariableType getVariableType() {
        return dynamic;
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
    public boolean isApplicable(Pin another) {
        if(isDynamicVariableSet()) {
            return super.isApplicable(another);
        }

        return another.isInput();
    }
}
