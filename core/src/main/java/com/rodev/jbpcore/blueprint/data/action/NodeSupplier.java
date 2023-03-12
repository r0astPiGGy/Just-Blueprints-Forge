package com.rodev.jbpcore.blueprint.data.action;

import com.rodev.jbpcore.blueprint.node.BPNode;
import com.rodev.jbpcore.blueprint.node.impl.NodeView;
import com.rodev.jbpcore.blueprint.pin.exec_pin.ExecPin;

public interface NodeSupplier {

    NodeSupplier identity = (color, action) -> {
        var node = new NodeView(color, action.id(), action.name(), action.createIcon());

        node.addInputPin(ExecPin.inputPin(), "");
        node.addOutputPin(ExecPin.outputPin(), "");

        return node;
    };

    BPNode create(int color, Action action);

}
