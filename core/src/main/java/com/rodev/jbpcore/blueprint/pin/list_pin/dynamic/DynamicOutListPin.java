package com.rodev.jbpcore.blueprint.pin.list_pin.dynamic;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.list_pin.InListPin;
import com.rodev.jbpcore.blueprint.pin.list_pin.ListPin;
import com.rodev.jbpcore.blueprint.pin.list_pin.OutListPin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamicOutListPin extends OutListPin {

    private VariableType dynamic;

    public DynamicOutListPin(PinType pinType, VariableType elementType) {
        super(pinType, elementType);

        dynamic = elementType;
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(isDynamicVariableSet()) {
            return super.isApplicable(another);
        }

        if(another.isOutput()) return false;

        return another instanceof ListPin;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public void setVariableType(VariableType variableType) {
        dynamic = variableType;
    }

    @Override
    public void resetVariableType() {
        dynamic = super.getElementType();
    }

    @Override
    public boolean isDynamicVariableSet() {
        return dynamic != super.getElementType();
    }

    @Override
    public @Nullable String getDependantId() {
        return null;
    }

    @Override
    public @NotNull VariableType getElementType() {
        return dynamic;
    }
}
