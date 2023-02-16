package com.rodev.test.blueprint.pin;

import icyllis.modernui.graphics.drawable.Drawable;

import java.util.Collection;

public interface Pin {

    Drawable createDrawable();

    Position getPosition();

    void onLineDraw(int xStart, int yStart, int xEnd, int yEnd);

    void onLineDrawEnd(int xStart, int yStart, int xEnd, int yEnd);

    void enable();

    void disable();

    int getColor();

    void onPinHovered();

    void onPinHoverStarted();

    void onPinHoverEnded();

    void onPositionUpdate();

    Collection<Pin> getConnections();

    void connect(Pin pin);

    void disconnect(Pin pin);

    void disconnectAll();

    boolean supportMultipleConnections();

    boolean isConnected();

    boolean isApplicable(Pin another);

    boolean isOutput();

    void setPinDragListener(PinDragListener pinDragListener);

    void setPinHoverListener(PinHoverListener pinHoverListener);

    void setPinToggleListener(PinToggleListener pinToggleListener);

    void setPositionSupplier(PinPositionSupplier positionSupplier);

    boolean isPinConnected(Pin pin);

    boolean isInput();

}
