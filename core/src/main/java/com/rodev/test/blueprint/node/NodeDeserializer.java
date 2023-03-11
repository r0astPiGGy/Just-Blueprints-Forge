package com.rodev.test.blueprint.node;

import com.rodev.test.blueprint.pin.Pin;

import java.util.List;
import java.util.Map;

public interface NodeDeserializer {

    BPNode getNode();

    void deserialize();

    Map<String, Pin> getOutputPins();

    List<PinConnection> getPinConnections();

}
