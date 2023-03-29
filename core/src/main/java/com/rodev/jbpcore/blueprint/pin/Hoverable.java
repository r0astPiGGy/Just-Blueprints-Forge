package com.rodev.jbpcore.blueprint.pin;

public interface Hoverable {

    void onPinHovered();

    void onPinHoverStarted();

    void onPinHoverEnded();

    void setPinHoverListener(PinHoverListener pinHoverListener);

}
