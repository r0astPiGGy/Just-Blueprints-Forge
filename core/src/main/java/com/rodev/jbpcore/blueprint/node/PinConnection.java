package com.rodev.jbpcore.blueprint.node;

import com.rodev.jbpcore.blueprint.pin.Pin;

public record PinConnection(BPNode node, String outputPin, Pin inputPin) {}
