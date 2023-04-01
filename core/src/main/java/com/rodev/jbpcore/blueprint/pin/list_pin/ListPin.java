package com.rodev.jbpcore.blueprint.pin.list_pin;

import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import org.jetbrains.annotations.NotNull;

public interface ListPin extends Pin {

    @NotNull
    VariableType getElementType();

    @Override
    default int getColor() {
        return getElementType().color();
    }
}
