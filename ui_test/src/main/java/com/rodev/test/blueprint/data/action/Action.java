package com.rodev.test.blueprint.data.action;

import com.rodev.test.blueprint.data.DataAccess;
import com.rodev.test.blueprint.data.action.type.ActionType;
import com.rodev.test.blueprint.node.BPNode;
import com.rodev.test.blueprint.pin.Pin;
import com.rodev.test.blueprint.pin.var_pin.VarPin;
import com.rodev.test.contextmenu.ContextMenuItem;
import com.rodev.test.contextmenu.ContextTreeNodeView;
import com.rodev.test.contextmenu.ContextTreeRootView;

import java.util.List;
import java.util.function.Consumer;

public record Action(String id, String name, ActionType actionType, List<PinType> inputPins, List<PinType> outputPins,
                     String category) {

    public void addTo(ContextTreeRootView contextTreeRootView, ContextMenuItem contextMenuItem) {
        ContextTreeNodeView treeNode = null;
        for(String id : category.split("\\.")) {
            if(treeNode == null) {
                treeNode = contextTreeRootView.getOrCreate(id);
            } else {
                treeNode = treeNode.getOrCreate(id);
            }
            var translated = DataAccess.translateCategoryId(id);
            if(translated == null) translated = id;

            treeNode.setName(translated);
        }

        if(treeNode == null) return;

        treeNode.add(contextMenuItem);
    }

    public BPNode toNode(Consumer<Pin> onPinCreated, Consumer<BPNode> onNodeCreated) {
        var node = actionType().createNode(onPinCreated, name);

        for (var inputPinType : inputPins()) {
            var pin = VarPin.inputPin(inputPinType.type());
            onPinCreated.accept(pin);
            node.addInputPin(pin, inputPinType.name());
        }

        for (var outputPinType : outputPins()) {
            var pin = VarPin.outputPin(outputPinType.type());
            onPinCreated.accept(pin);
            node.addOutputPin(pin, outputPinType.name());
        }

        onNodeCreated.accept(node);

        return node;
    }
}
