package com.rodev.jbpcore.blueprint.pin;

public interface PinConnectionListener {

    void onConnected(Pin pin);

    void onDisconnected(Pin pin);

}
