package com.rodev.test.blueprint.data.action.type;


import com.rodev.test.blueprint.data.action.NodeSupplier;
import com.rodev.test.blueprint.node.BPNode;
import com.rodev.test.blueprint.pin.Pin;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public final class ActionType {
    private final String type;
    private final int headerColor;
    private final NodeSupplier nodeSupplier;
    public ActionType(String type, int headerColor, NodeSupplier nodeSupplier) {
        this.type = type;
        this.headerColor = headerColor;
        this.nodeSupplier = nodeSupplier;
    }

    public String type() {
        return type;
    }

    public BPNode createNode(Consumer<Pin> onPinCreated, String name) {
        return nodeSupplier.create(onPinCreated, headerColor, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ActionType) obj;
        return Objects.equals(this.type, that.type) &&
                this.headerColor == that.headerColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, headerColor);
    }

    @Override
    public String toString() {
        return "ActionType[" +
                "type=" + type + ", " +
                "headerColor=" + headerColor + ']';
    }

}
