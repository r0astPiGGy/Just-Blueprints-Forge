package com.rodev.jbpcore.blueprint.pin.dynamic;

import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Dynamic {

    boolean isDynamic();

    void setVariableType(VariableType variableType);

    void resetVariableType();

    /**
     * @return if this dynamic pin's VariableType got changed after connection
     */
    boolean isDynamicVariableSet();

    /**
     * @return id of the dependant pin, otherwise null if self is root or not dynamic
     */
    @Nullable
    String getDependantId();

    @NotNull
    VariableType getDependantType(@NotNull Pin dependantPin);

    void setVariableTypeChangedListener(PinVariableTypeChangeListener listener);

    void invokeVariableTypeChanged();

}
