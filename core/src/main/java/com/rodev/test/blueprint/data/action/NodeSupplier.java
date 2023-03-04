package com.rodev.test.blueprint.data.action;

import com.rodev.test.blueprint.node.BPNode;
import com.rodev.test.blueprint.node.impl.NodeView;
import com.rodev.test.blueprint.pin.exec_pin.ExecPin;

public interface NodeSupplier {

    NodeSupplier identity = (color, action) -> {
        var node = new NodeView(color, action.id(), action.name(), action.createIcon());

        node.addInput(ExecPin.inputPin().createRowView());
        node.addOutput(ExecPin.outputPin().createRowView());

        return node;
    };

    BPNode create(int color, Action action);

}
