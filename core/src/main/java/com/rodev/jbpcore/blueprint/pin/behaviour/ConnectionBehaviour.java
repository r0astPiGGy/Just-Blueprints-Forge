package com.rodev.jbpcore.blueprint.pin.behaviour;

import com.rodev.jbpcore.blueprint.pin.Pin;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public interface ConnectionBehaviour {

    boolean isApplicable(Pin target, Pin pin);

    boolean isInput();

    boolean isOutput();

    boolean supportMultipleConnections();

    Collection<Pin> getConnections();

    @Nullable
    Pin getFirstConnectedPin();

    void onPinConnected(Pin connection);

    void onPinDisconnected(Pin connection);

    void onAllPinsDisconnected();

    boolean isConnected();

    boolean isConnectedTo(Pin pin);

}
