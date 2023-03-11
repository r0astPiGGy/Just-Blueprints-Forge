package com.rodev.test.blueprint.node;

import com.rodev.test.blueprint.pin.Pin;

public record PinConnection(BPNode node, String outputPin, Pin inputPin) {}
