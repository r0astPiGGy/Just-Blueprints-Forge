package com.rodev.jbpcore.blueprint.pin.dynamic;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public enum DynamicPinDestination {
    LIST_ELEMENT("element-type"),
    MAP_KEY("key-type"),
    MAP_VALUE("value-type"),
    TYPE("type")

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
    public static DynamicPinDestination fromDestination(@NotNull String destination) {
        var dest = dynamicPinDestinationMap.get(destination);

        Objects.requireNonNull(dest);

        return dest;
    }


}
