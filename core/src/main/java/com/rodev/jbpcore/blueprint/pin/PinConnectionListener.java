package com.rodev.jbpcore.blueprint.pin;

public interface PinConnectionListener {

    void onConnect(Pin target, Pin connection);

    void onDisconnect(Pin target, Pin connection);

}
