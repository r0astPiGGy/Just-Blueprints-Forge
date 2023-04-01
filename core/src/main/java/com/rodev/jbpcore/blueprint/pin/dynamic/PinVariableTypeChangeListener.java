package com.rodev.jbpcore.blueprint.pin.dynamic;

import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import org.jetbrains.annotations.Nullable;

public interface PinVariableTypeChangeListener {

    void onVariableTypeChange(Pin pin, VariableType currentType, @Nullable VariableType newType);

}
