package com.rodev.jbpcore.blueprint.pin;

import java.util.Collection;

public interface Connectable {

    Collection<Pin> getConnections();

    boolean connect(Pin pin);

    boolean disconnect(Pin pin);

    boolean disconnectAll();

    boolean supportMultipleConnections();

    boolean isConnected();

    boolean isApplicable(Connectable another);

    void setIsBeingConnected(boolean value);

    boolean isBeingConnected();

    void setIsBeingDisconnected(boolean value);

    boolean isBeingDisconnected();

}
