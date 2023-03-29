package com.rodev.jbpcore.blueprint.pin;

public interface Toggleable {

    void enable();

    void disable();

    void setPinToggleListener(PinToggleListener pinToggleListener);

}
