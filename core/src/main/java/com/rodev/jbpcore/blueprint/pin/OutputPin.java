package com.rodev.jbpcore.blueprint.pin;

import com.rodev.jbpcore.blueprint.data.action.PinType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class OutputPin extends AbstractPin {

    private final Set<Pin> connectionSet = new HashSet<>();

    public OutputPin(PinType pinType) {
        super(pinType);
    }

    public OutputPin(PinType pinType, UUID uuid) {
        super(pinType, uuid);
    }

    @Override
    public Collection<Pin> getConnections() {
        return connectionSet;
    }

    @Override
    public PinRowView createRowView() {
        return PinRowView.rightDirectedRow(new PinView(this), "");
    }

    @Override
    public void connect(Pin pin) {
        if (pin == this) return;

        if (connectionSet.isEmpty()) {
            enable();
        }

        connectionSet.add(pin);
    }

    @Override
    public void disconnect(Pin pin) {
        if (pin == this) return;

        connectionSet.remove(pin);

        if(connectionSet.isEmpty()) {
            disable();
        }
    }

    @Override
    public void disconnectAll() {
        connectionSet.clear();
        disable();
    }

    @Override
    public boolean supportMultipleConnections() {
        return true;
    }

    @Override
    public boolean isConnected() {
        return !connectionSet.isEmpty();
    }

    @Override
    public boolean isOutput() {
        return true;
    }

    @Override
    public boolean isPinConnected(Pin pin) {
        return connectionSet.contains(pin);
    }

    @Override
    public boolean isInput() {
        return false;
    }
}
