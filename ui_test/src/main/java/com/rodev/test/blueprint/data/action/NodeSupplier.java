package com.rodev.test.blueprint.data.action;

import com.rodev.test.blueprint.node.BPNode;
import com.rodev.test.blueprint.node.NodeView;
import com.rodev.test.blueprint.pin.Pin;
import com.rodev.test.blueprint.pin.exec_pin.ExecPin;

import java.util.function.Consumer;

public interface NodeSupplier {

    NodeSupplier identity = (onPinCreated, color, name) -> {
        var node = new NodeView(color, name);

        var input = ExecPin.inputPin();
        onPinCreated.accept(input);

        var output = ExecPin.outputPin();
        onPinCreated.accept(output);

        node.addInput(input.createRowView());
        node.addOutput(output.createRowView());

        return node;
    };

    BPNode create(Consumer<Pin> onPinCreated, int color, String name);

}
