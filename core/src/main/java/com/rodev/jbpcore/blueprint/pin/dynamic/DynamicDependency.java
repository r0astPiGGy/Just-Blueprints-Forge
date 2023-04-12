package com.rodev.jbpcore.blueprint.pin.dynamic;

public record DynamicDependency(String dependsOn, String dependType) {

    public DynamicPinDestination getDestination() {
        return DynamicPinDestination.fromDestination(dependType);
    }

}
