package com.rodev.jbpcore.blueprint.pin;

import com.rodev.jbpcore.blueprint.data.action.PinType;

import java.util.List;
import java.util.UUID;

public abstract class InputPin extends AbstractPin {

    private Pin connectedPin;

    public InputPin(PinType pinType) {
        super(pinType);
    }

    public InputPin(PinType pinType, UUID uuid) {
        super(pinType, uuid);
    }

    @Override
    public List<Pin> getConnections() {
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
    public PinRowView createRowView() {
        return PinRowView.leftDirectedRow(new PinView(this), "");
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
