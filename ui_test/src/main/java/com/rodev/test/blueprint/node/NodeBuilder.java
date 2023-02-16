package com.rodev.test.blueprint.node;

import com.rodev.test.blueprint.pin.Pin;
import com.rodev.test.blueprint.pin.PinRowView;
import com.rodev.test.blueprint.pin.PinView;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public class NodeBuilder {

    private final String functionName;
    private boolean isSimple = false;
    private final List<Supplier<PinRowView>> outputPinSuppliers = new LinkedList<>();
    private final List<Supplier<PinRowView>> inputPinSuppliers = new LinkedList<>();

    private NodeBuilder(String functionName) {
        this.functionName = functionName;
    }

    public NodeBuilder addPin(Pin pin, String name) {
        if(pin.isInput()) {
            return addInputPin(pin, name);
        }
        if(pin.isOutput()) {
            return addOutputPin(pin, name);
        }

        return this;
    }

    public NodeBuilder addOutputPin(Pin pin, String name) {
        outputPinSuppliers.add(() -> PinRowView.rightDirectedRow(new PinView(pin), name));
        return this;
    }

    public NodeBuilder addInputPin(Pin pin, String name) {
        inputPinSuppliers.add(() -> PinRowView.leftDirectedRow(new PinView(pin), name));
        return this;
    }

    public NodeBuilder setSimple(boolean isSimple) {
        this.isSimple = isSimple;
        return this;
    }

    public NodeView build() {
        var nodeView = new NodeView(functionName);

        inputPinSuppliers.forEach(s -> {
            nodeView.addInput(s.get());
        });
        outputPinSuppliers.forEach(s -> {
            nodeView.addOutput(s.get());
        });

        return nodeView;
    }

    public static NodeBuilder builder(String functionName) {
        return new NodeBuilder(functionName);
    }

}
