package com.rodev.jbpcore.blueprint.pin.dynamic;

import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.list_pin.ListPin;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum DynamicPinDestination {
    LIST_ELEMENT("element-type") {
        @Override
        public @NotNull VariableType resolveVariableTypeInPin(Pin pin) {
            return pin.getType().getVariableType();
//            if (pin instanceof ListPin listPin) {
//                return listPin.getElementType();
//            }
//
//            throw new IllegalArgumentException("Provided pin is not list");
        }
    },
    MAP_KEY("key-type") {
        @Override
        public @NotNull VariableType resolveVariableTypeInPin(Pin pin) {
            throw new IllegalStateException("Stub!");
        }
    },
    MAP_VALUE("value-type") {
        @Override
        public @NotNull VariableType resolveVariableTypeInPin(Pin pin) {
            throw new IllegalStateException("Stub!");
        }
    },
    DEFAULT("default")

    ;

    private final String id;

    private static final Map<String, DynamicPinDestination> dynamicPinDestinationMap;

    static {
        dynamicPinDestinationMap = new HashMap<>();
        for (var value : DynamicPinDestination.values()) {
            dynamicPinDestinationMap.put(value.id, value);
        }
    }

    @NotNull
    public static DynamicPinDestination fromDestination(@Nullable String destination) {
        if(destination == null) return DEFAULT;

        return dynamicPinDestinationMap.getOrDefault(destination, DEFAULT);
    }

    @NotNull
    public VariableType resolveVariableTypeInPin(Pin pin) {
        return pin.getType().getVariableType();
    }


}
