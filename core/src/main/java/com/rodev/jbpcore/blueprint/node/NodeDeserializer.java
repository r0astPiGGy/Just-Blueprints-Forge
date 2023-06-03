package com.rodev.jbpcore.blueprint.node;

import com.rodev.jbpcore.blueprint.pin.Pin;

import java.util.List;
import java.util.Map;

public interface NodeDeserializer {

    GraphNode getNode();

    void deserialize();

    Map<String, Pin> getOutputPins();

    List<PinConnection> getPinConnections();

}
