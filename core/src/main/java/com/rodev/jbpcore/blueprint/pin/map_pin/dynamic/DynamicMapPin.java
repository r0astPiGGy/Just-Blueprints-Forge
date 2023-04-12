package com.rodev.jbpcore.blueprint.pin.map_pin.dynamic;

import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.dynamic.Dynamic;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicBehaviour;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicGroup;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicPinDestination;
import com.rodev.jbpcore.blueprint.pin.map_pin.MapPin;
import org.jetbrains.annotations.NotNull;

public interface DynamicMapPin extends MapPin, Dynamic {

    @Override
    default void setVariableType(@NotNull VariableType variableType) {
        throw new IllegalStateException("Not supported");
    }

    @Override
    default DynamicGroup createDynamicGroup() {
        return DynamicGroup.of(this,
                DynamicBehaviour.of(
                        DynamicPinDestination.MAP_KEY,
                        this::setKeyType,
                        this::resetKeyType
                ),
                DynamicBehaviour.of(
                        DynamicPinDestination.MAP_VALUE,
                        this::setValueType,
                        this::resetValueType
                )
        );
    }

    void setKeyType(VariableType variableType);

    void resetKeyType();

    void setValueType(VariableType variableType);

    void resetValueType();

    @Override
    default void resetVariableType() {
        resetKeyType();
        resetValueType();
    }

    boolean isDynamicKeySet();

    boolean isDynamicValueSet();
}
