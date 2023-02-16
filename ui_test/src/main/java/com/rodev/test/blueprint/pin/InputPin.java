package com.rodev.test.blueprint.pin;

import com.rodev.test.blueprint.pin.AbstractPin;
import com.rodev.test.blueprint.pin.Pin;

import java.util.Collection;
import java.util.List;

public abstract class InputPin extends AbstractPin {

    private Pin connectedPin;

    public InputPin(int color) {
        super(color);
    }

    @Override
    public Collection<Pin> getConnections() {
        if (connectedPin == null) return List.of();

        return List.of(connectedPin);
    }

    @Override
    public void connect(Pin pin) {
        if(pin == this) return;

        connectedPin = pin;
        enable();
    }

    @Override
    public void disconnect(Pin pin) {
        disconnectAll();
    }

    @Override
    public void disconnectAll() {
        if(connectedPin == null) return;

        disable();
        connectedPin = null;
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
    public boolean isPinConnected(Pin pin) {
        return pin == connectedPin;
    }

    @Override
    public boolean isInput() {
        return true;
    }
}
