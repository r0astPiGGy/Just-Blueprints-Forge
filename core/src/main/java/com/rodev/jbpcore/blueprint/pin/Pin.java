package com.rodev.jbpcore.blueprint.pin;

import com.rodev.jbpcore.blueprint.data.action.PinType;
import icyllis.modernui.graphics.drawable.Drawable;

import java.util.Collection;
import java.util.UUID;

public interface Pin extends Dynamic, Hoverable, Draggable, Positionable, Toggleable {

    UUID getId();

    void setId(UUID id);

    PinRowView createRowView();

    Drawable createDrawable();

    PinType getType();

    int getColor();

    boolean isApplicable(Pin another);

    boolean isInput();

    boolean isOutput();

    void setPinConnectionHandler(PinConnectionHandler pinConnectionHandler);

    Collection<Pin> getConnections();

    boolean connect(Pin pin);

    boolean disconnect(Pin pin);

    boolean disconnectAll();

    boolean supportMultipleConnections();

    boolean isConnected();

    boolean isConnectedTo(Pin pin);

    void setIsBeingConnected(boolean value);
    
    boolean isBeingConnected();
    
    void setIsBeingDisconnected(boolean value);

    boolean isBeingDisconnected();

}
