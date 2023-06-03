package com.rodev.jbpcore.data.action.type;


import com.rodev.jbpcore.data.action.Action;
import com.rodev.jbpcore.data.action.NodeSupplier;
import com.rodev.jbpcore.blueprint.node.GraphNode;

import java.util.Objects;

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

    public GraphNode createNode(Action action) {
        return nodeSupplier.create(headerColor, action);
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
