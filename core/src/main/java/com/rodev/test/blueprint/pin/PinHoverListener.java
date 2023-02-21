package com.rodev.test.blueprint.pin;

public interface PinHoverListener {

    void onPinHoverStarted(Pin pin);

    default void onPinHovered(Pin pin) {}

    void onPinHoverEnded(Pin pin);

}
