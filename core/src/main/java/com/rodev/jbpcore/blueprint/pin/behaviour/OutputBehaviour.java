package com.rodev.jbpcore.blueprint.pin.behaviour;

import com.rodev.jbpcore.blueprint.pin.Pin;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class OutputBehaviour implements ConnectionBehaviour {
    private final Set<Pin> connectionSet = new HashSet<>();

    @Override
    public Collection<Pin> getConnections() {
        return connectionSet;
    }

    @Override
    public @Nullable Pin getFirstConnectedPin() {
        return connectionSet.stream().findFirst().orElse(null);
    }

    @Override
    public void onPinConnected(Pin connection) {
        connectionSet.add(connection);
    }

    @Override
    public void onPinDisconnected(Pin connection) {
        connectionSet.remove(connection);
    }

    @Override
    public void onAllPinsDisconnected() {
        connectionSet.clear();
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
    public boolean isConnectedTo(Pin pin) {
        Objects.requireNonNull(pin);

        return connectionSet.contains(pin);
    }

    @Override
    public boolean isInput() {
        return false;
    }
}
