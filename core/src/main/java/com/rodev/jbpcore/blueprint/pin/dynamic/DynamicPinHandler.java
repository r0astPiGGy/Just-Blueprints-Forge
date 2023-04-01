package com.rodev.jbpcore.blueprint.pin.dynamic;

import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.PinConnectionListener;

import java.util.*;
import java.util.function.Consumer;

public class DynamicPinHandler {

    private final Map<String, Pin> inputPins = new HashMap<>();
    private final Map<String, Pin> outputPins = new HashMap<>();

    private final Map<UUID, DynamicGroup> dynamicGroupsByRootId = new HashMap<>();

    public void addDynamicPin(Pin pin) {
        var id = pin.getType().getId();

        if(pin.isInput()) {
            inputPins.put(id, pin);
        }

        if(pin.isOutput()) {
            outputPins.put(id, pin);
        }
    }

    // TODO: rework
    public void resolveDynamicGroups() {
        iterateInputPins();
        iterateOutputPins();
    }

    private void iterateInputPins() {
        for (var pin : inputPins.values()) {
            var group = new DynamicGroup(pin);

            dynamicGroupsByRootId.put(pin.getId(), group);
        }
    }

    private void iterateOutputPins() {
        for (var pin : outputPins.values()) {
            var dynamicPin = (Dynamic) pin;
            var dependantPin = inputPins.get(dynamicPin.getDependantId());
            if(dependantPin == null)
                throw new IllegalStateException("Cannot be null");

            var dynamicGroup = dynamicGroupsByRootId.get(dependantPin.getId());

            dynamicGroup.addDependency(pin);
        }
    }

    private static class DynamicGroup implements PinConnectionListener {

        private final List<Pin> dependencies = new LinkedList<>();

        private int connectionCounter = 0;

        private DynamicGroup(Pin root) {
            addDependency(root);
        }

        public void addDependency(Pin pin) {
            dependencies.add(pin);
            applyListenersTo(pin);
        }

        private void applyListenersTo(Pin pin) {
            pin.setPinConnectionListener(this);
        }

        @Override
        public void onConnect(Pin target, Pin connection) {
            connectionCounter++;

            var fromTheGroup = resolveOurPin(target, connection);

            if(fromTheGroup == null)
                throw new IllegalStateException("Couldn't get here.");

            if(fromTheGroup.isDynamicVariableSet()) return;

            // todo rework

            var firstConnected = fromTheGroup.getFirstConnectedPin();

            if(firstConnected == null)
                throw new IllegalStateException("Couldn't be null");

            var type = fromTheGroup.getDependantType(firstConnected);

            forEach(p -> {
                p.setVariableType(type);
                p.invokeVariableTypeChanged();
            });
        }

        private Pin resolveOurPin(Pin... pins) {
            for (Pin pin : pins) {
                if(dependencies.contains(pin)) return pin;
            }

            return null;
        }

        @Override
        public void onDisconnect(Pin target, Pin connection) {
            connectionCounter--;

            if(connectionCounter > 0) return;

            resetAll();
        }

        private void resetAll() {
            forEach(d -> {
                d.resetVariableType();
                d.invokeVariableTypeChanged();
            });
        }

        private void forEach(Consumer<Pin> pinConsumer) {
            dependencies.forEach(pinConsumer);
        }
    }
}
