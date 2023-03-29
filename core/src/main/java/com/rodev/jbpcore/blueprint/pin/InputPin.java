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
    public boolean connect(Pin pin) {
        if(!handleOnConnect(pin)) return false;

        connectedPin = pin;
        enable();

        return true;
    }

    @Override
    public PinRowView createRowView() {
        return PinRowView.leftDirectedRow(new PinView(this), "");
    }

    @Override
    public boolean disconnect(Pin pin) {
        if(!handleOnDisconnect(pin)) return true;

        disable();
        connectedPin = null;

        return true;
    }

    @Override
    public boolean disconnectAll() {
        if(connectedPin == null) return false;

        return disconnect(connectedPin);
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
        return pin == connectedPin;
    }

    @Override
    public boolean isInput() {
        return true;
    }
}
