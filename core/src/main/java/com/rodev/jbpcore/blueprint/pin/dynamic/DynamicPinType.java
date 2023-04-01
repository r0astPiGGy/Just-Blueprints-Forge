package com.rodev.jbpcore.blueprint.pin.dynamic;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.var_pin.dynamic.DynamicInVarPin;
import com.rodev.jbpcore.blueprint.pin.var_pin.dynamic.DynamicOutVarPin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamicPinType extends PinType {

    private final String dependsOn;
    private final DynamicPinDestination dependValueDestination;
    private VariableType variableType;

    public DynamicPinType(String id, String name, VariableType type, String dependsOn, String dependValueDestination) {
        super(id, name, type);
        this.dependsOn = dependsOn;
        this.dependValueDestination = DynamicPinDestination.fromDestination(dependValueDestination);

        resetVariableType();
    }

    public void setVariableType(VariableType variableType) {
        this.variableType = variableType;
    }

    public void resetVariableType() {
        variableType = super.getVariableType();
    }

    @Override
    public Pin createInputPin() {
        return new DynamicInVarPin(this.clone());
    }

    @Override
    public Pin createOutputPin() {
        return new DynamicOutVarPin(this.clone());
    }

    @Nullable
    public String getDependsOn() {
        return dependsOn;
    }

    @NotNull
    public VariableType resolveVariableTypeInPin(Pin pin) {
        return dependValueDestination.resolveVariableTypeInPin(pin);
    }

    public boolean isDynamicVariableTypeSet() {
        return getVariableType() != super.getVariableType();
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public VariableType getVariableType() {
        return variableType;
    }
}
