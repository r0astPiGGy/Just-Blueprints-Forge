package com.rodev.jbpcore.blueprint.pin.var_pin.dynamic;

import com.rodev.jbpcore.blueprint.pin.behaviour.ConnectionBehaviour;
import com.rodev.jbpcore.blueprint.pin.var_pin.VarPin;
import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.dynamic.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class DynamicVarPin extends VarPin implements Dynamic {

    private final VariableType defaultType;
    private final DynamicPinType pinType;
    private VariableType dynamic;

    protected DynamicVarPin(DynamicPinType pinType, ConnectionBehaviour connectionBehaviour) {
        super(pinType, connectionBehaviour);

        this.pinType = pinType;

        dynamic = defaultType = pinType.getVariableType();
    }

    public static DynamicVarPin outputPin(DynamicPinType pinType) {
        return new DynamicVarPin(pinType, new DynamicOutputVarBehaviour());
    }

    public static DynamicVarPin inputPin(DynamicPinType pinType) {
        return new DynamicVarPin(pinType, new DynamicInputVarBehaviour());
    }

    @Override
    public DynamicPinType getType() {
        return pinType;
    }

    @Override
    public boolean isDynamicRoot() {
        return getType().getDependsOn() == null;
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
}
