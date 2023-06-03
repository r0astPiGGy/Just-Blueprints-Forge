package com.rodev.jbpcore.blueprint.pin.dynamic.impl;

import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.dynamic.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DynamicGroupImpl implements DynamicGroup {

    private int connectionCounter = 0;
    private final Map<DynamicPinDestination, Wrappers> wrappersMap = new HashMap<>();

    public DynamicGroupImpl(Dynamic root, DynamicBehaviour dynamicBehaviour, DynamicBehaviour... behaviours) {
        applyListenersTo(root);

        addDependency(root, dynamicBehaviour);

        for (var behaviour : behaviours) {
            addDependency(root, behaviour);
        }
    }

    @Override
    public void addDependency(Dynamic pin, DynamicBehaviour dynamicBehaviour) {
        PinWrapper wrapper = new PinWrapper(pin, dynamicBehaviour);
        getWrappers(dynamicBehaviour.getDestination()).add(wrapper);
        applyListenersTo(pin);
    }

    @NotNull
    private Wrappers getWrappers(DynamicPinDestination destination) {
        Wrappers wrappers = wrappersMap.get(destination);

        if(wrappers == null) {
            wrappers = new Wrappers();
            wrappersMap.put(destination, wrappers);
        }

        return wrappers;
    }

    private void applyListenersTo(Pin pin) {
        pin.setPinConnectionListener(this);
    }

    @Override
    public void onConnect(Pin target, Pin connection) {
        Dynamic dynamicTarget = (Dynamic) target;

        connectionCounter++;

        var connectionMap = dynamicTarget.getAffectedDestinationsFromConnection(connection);
        incrementCountersIn(connectionMap.keySet());

        updateVariableTypesIn(connectionMap);
    }

    private void incrementCountersIn(Collection<DynamicPinDestination> destinations) {
        for (var destination : destinations) {
            getWrappers(destination).increment();
        }
    }

    private void decrementCountersIn(Collection<DynamicPinDestination> destinations) {
        for (var destination : destinations) {
            var wrappers = getWrappers(destination);

            wrappers.decrement();
            wrappers.resetIfNoConnections();
        }
    }

    private void updateVariableTypesIn(Map<DynamicPinDestination, VariableType> destinations) {
        destinations.forEach((dest, type) -> {
            getWrappers(dest).setType(type);
        });
    }

    @Override
    public void onDisconnect(Pin target, Pin connection) {
        connectionCounter--;

        Dynamic dynamicPin = (Dynamic) target;

        var map = dynamicPin.getAffectedDestinationsFromConnection(connection);

        decrementCountersIn(map.keySet());

        if(connectionCounter > 0) return;

        resetAll();
    }

    private void resetAll() {
        wrappersMap.values().forEach(Wrappers::resetAll);
    }

    private static class Wrappers {
        private final List<PinWrapper> wrappers = new LinkedList<>();
        private int counter = 0;

        public void add(PinWrapper wrapper) {
            wrappers.add(wrapper);
        }

        public void setType(@NotNull VariableType type) {
            if(counter > 1) {
                return;
            }

            for (PinWrapper wrapper : wrappers) {
                wrapper.setVariableType(type);
            }
        }

        public void increment() {
            counter++;
        }

        public void decrement() {
            counter--;
        }

        public void resetIfNoConnections() {
            if(counter > 0) return;

            for (PinWrapper wrapper : wrappers) {
                wrapper.reset();
            }
        }

        public void resetAll() {
            wrappers.forEach(PinWrapper::reset);
        }
    }

    private record PinWrapper(Dynamic pin, DynamicBehaviour dynamicBehaviour) {

        public void setVariableType(@NotNull VariableType variableType) {
            dynamicBehaviour.onTypeSet(variableType);
            pin.invokeVariableTypeChanged();
        }

        public void reset() {
            dynamicBehaviour.onTypeReset();
            pin.invokeVariableTypeChanged();
        }
    }
}
