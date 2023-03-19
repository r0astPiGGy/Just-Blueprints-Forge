package com.rodev.jbpcore.blueprint.pin;

public interface PinConnectionHandler {

    boolean handleConnect(Pin target, Pin connection);

    boolean handleDisconnect(Pin target, Pin connection);

    boolean onDisconnectedAll(Pin target);

}
