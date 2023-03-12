package com.rodev.jbpcore.blueprint.pin;

public interface PinHoverListener {

    void onPinHoverStarted(Pin pin);

    default void onPinHovered(Pin pin) {}

    void onPinHoverEnded(Pin pin);

}
