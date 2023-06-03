package com.rodev.jbpcore.blueprint.pin.behaviour;

import com.rodev.jbpcore.blueprint.pin.Pin;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public abstract class InputBehaviour implements ConnectionBehaviour {

    private Pin connectedPin;

    @Override
    public List<Pin> getConnections() {
        if (connectedPin == null) return List.of();

        return List.of(connectedPin);
    }

    @Override
    public @Nullable Pin getFirstConnectedPin() {
        return connectedPin;
    }

    @Override
    public void onPinConnected(Pin connection) {
        connectedPin = connection;
    }

    @Override
    public void onPinDisconnected(Pin connection) {
        connectedPin = null;
    }

    @Override
    public void onAllPinsDisconnected() {
        onPinDisconnected(connectedPin);
    }

    @Override
    public boolean supportMultipleConnections() {
        return false;
    }

    @Override
    public boolean isConnected() {
        return connectedPin != null;
    }

    @Override
    public boolean isOutput() {
        return false;
    }

    @Override
    public boolean isConnectedTo(Pin pin) {
        Objects.requireNonNull(pin);

        return pin == connectedPin;
    }

    @Override
    public boolean isInput() {
        return true;
    }
}
