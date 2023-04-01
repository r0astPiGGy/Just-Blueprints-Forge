package com.rodev.jbpcore.blueprint.pin.list_pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.InputPin;
import com.rodev.jbpcore.blueprint.pin.Pin;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class InListPin extends InputPin implements ListPin {

    @Getter
    private final VariableType elementType;

    public InListPin(PinType pinType, VariableType elementType) {
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
            return listPin.isOutput();
        }

        if(listPin instanceof InListPin) return false;

        var anotherElementType = listPin.getElementType();

        return getElementType().equals(anotherElementType) && another.isOutput();
    }
}
