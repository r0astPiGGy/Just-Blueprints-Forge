package com.rodev.jbpcore.blueprint.pin;

public interface Positionable {

    Position getPosition();

    void onPositionUpdate();

    void setPositionSupplier(PositionSupplier positionSupplier);

}
