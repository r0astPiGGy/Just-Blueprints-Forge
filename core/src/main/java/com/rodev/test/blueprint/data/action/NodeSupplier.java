package com.rodev.test.blueprint.data.action;

import com.rodev.test.blueprint.node.BPNode;
import com.rodev.test.blueprint.node.NodeView;
import com.rodev.test.blueprint.pin.Pin;
import com.rodev.test.blueprint.pin.exec_pin.ExecPin;

import java.util.function.Consumer;

public interface NodeSupplier {

    NodeSupplier identity = (color, action) -> {
        var node = new NodeView(color, action.id(), action.name());

        node.addInput(ExecPin.inputPin().createRowView());
        node.addOutput(ExecPin.outputPin().createRowView());

        return node;
    };

    BPNode create(int color, Action action);

}
