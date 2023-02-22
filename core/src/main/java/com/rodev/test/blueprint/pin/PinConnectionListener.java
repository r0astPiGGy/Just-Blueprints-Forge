package com.rodev.test.blueprint.pin;

public interface PinConnectionListener {

    void onConnected(Pin pin);

    void onDisconnected(Pin pin);

}
