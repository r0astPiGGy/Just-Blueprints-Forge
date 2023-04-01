package com.rodev.jbpcore.blueprint.pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class InputPin extends AbstractPin {

    private Pin connectedPin;

    public InputPin(PinType pinType) {
        super(pinType);
    }

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
    public boolean connect(Pin pin) {
        if(!handleOnConnect(pin)) return false;

        connectedPin = pin;

        invokePinConnected(pin);

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

        invokePinDisconnected(pin);

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
