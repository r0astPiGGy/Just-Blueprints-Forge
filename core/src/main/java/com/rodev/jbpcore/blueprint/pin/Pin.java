package com.rodev.jbpcore.blueprint.pin;

import com.rodev.jbpcore.blueprint.data.action.PinType;
import icyllis.modernui.graphics.drawable.Drawable;

import java.util.Collection;
import java.util.UUID;

public interface Pin {

    UUID getId();

    void setId(UUID id);

    PinRowView createRowView();

    Drawable createDrawable();

    Position getPosition();

    void onLineDraw(int xStart, int yStart, int xEnd, int yEnd);

    void onLineDrawEnd(int xStart, int yStart, int xEnd, int yEnd);

    void enable();

    void disable();

    PinType getType();

    int getColor();

    void onPinHovered();

    void onPinHoverStarted();

    void onPinHoverEnded();

    void onPositionUpdate();

    Collection<Pin> getConnections();

    boolean connect(Pin pin);

    boolean disconnect(Pin pin);

    boolean disconnectAll();

    boolean supportMultipleConnections();

    boolean isConnected();

    boolean isApplicable(Pin another);

    boolean isOutput();

    void setPinDragListener(PinDragListener pinDragListener);

    void setPinHoverListener(PinHoverListener pinHoverListener);

    void setPinToggleListener(PinToggleListener pinToggleListener);

    void setPinConnectionHandler(PinConnectionHandler pinConnectionHandler);

    void setPositionSupplier(PinPositionSupplier positionSupplier);

    boolean isPinConnected(Pin pin);

    boolean isInput();

    void setIsBeingDisconnected(boolean value);

    boolean isBeingDisconnected();

}
