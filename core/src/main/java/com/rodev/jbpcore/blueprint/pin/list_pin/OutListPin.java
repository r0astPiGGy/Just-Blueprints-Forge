package com.rodev.jbpcore.blueprint.pin.list_pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.InputPin;
import com.rodev.jbpcore.blueprint.pin.OutputPin;
import com.rodev.jbpcore.blueprint.pin.Pin;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class OutListPin extends OutputPin implements ListPin {

    @Getter
    private final VariableType elementType;

    public OutListPin(PinType pinType, VariableType elementType) {
        super(pinType);

        this.elementType = elementType;
    }

    @Override
    public @NotNull VariableType getDependantType(@NotNull Pin dependantPin) {
        return ((ListPin) dependantPin).getElementType();
    }

    @Override
    public boolean isApplicable(Pin another) {
        if(!(another instanceof ListPin listPin)) return false;

        if(listPin.isDynamic() && !listPin.isDynamicVariableSet()) {
            return listPin.isInput();
        }

        if(listPin instanceof OutListPin) return false;

        var anotherElementType = listPin.getElementType();

        return getElementType().equals(anotherElementType) && another.isInput();
    }
}
