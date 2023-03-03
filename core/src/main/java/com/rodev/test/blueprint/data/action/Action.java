package com.rodev.test.blueprint.data.action;

import com.rodev.test.blueprint.data.DataAccess;
import com.rodev.test.blueprint.data.IconSupplier;
import com.rodev.test.blueprint.data.action.type.ActionType;
import com.rodev.test.blueprint.data.variable.VariableType;
import com.rodev.test.blueprint.node.BPNode;
import com.rodev.test.blueprint.pin.var_pin.VarPin;
import com.rodev.test.contextmenu.Item;
import com.rodev.test.contextmenu.tree.ContextTreeNodeView;
import com.rodev.test.contextmenu.tree.ContextTreeRootView;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class Action {
    private final String id;
    private final String name;
    private final ActionType actionType;
    private final List<PinType> inputPins;
    private final List<PinType> outputPins;
    private final String category;
    private final Object extraData;
    private final Set<VariableType> acceptableOutputPins = new HashSet<>();
    private final Set<VariableType> acceptableInputPins = new HashSet<>();

    @Getter
    @Setter
    private IconSupplier iconSupplier = IconSupplier.actionIconSupplier;

    public Action(String id, String name, ActionType actionType, List<PinType> inputPins, List<PinType> outputPins,
                  String category, Object extraData) {
        this.id = id;
        this.name = name;
        this.actionType = actionType;
        this.inputPins = inputPins;
        this.outputPins = outputPins;
        this.category = category;
        this.extraData = extraData;

        fillAcceptablePins();
    }

    private void fillAcceptablePins() {
        inputPins.forEach(pin -> {
            acceptableInputPins.add(pin.getVariableType());
        });
        outputPins.forEach(pin -> {
            acceptableOutputPins.add(pin.getVariableType());
        });
    }

    public boolean acceptsOutputType(VariableType outputType) {
        return acceptableOutputPins.contains(outputType);
    }

    public boolean acceptsInputType(VariableType inputType) {
        return acceptableInputPins.contains(inputType);
    }

    public void addTo(ContextTreeRootView contextTreeRootView, Item item) {
        ContextTreeNodeView treeNode = null;
        for (String id : category.split("\\.")) {
            if (treeNode == null) {
                treeNode = contextTreeRootView.getOrCreate(id);
            } else {
                treeNode = treeNode.getOrCreate(id);
            }
            var translated = DataAccess.translateCategoryId(id);
            if (translated == null) translated = id;

            treeNode.setName(translated);
        }

        if (treeNode == null) return;

        treeNode.add(item);
    }

    public BPNode toNode() {
        var node = actionType().createNode(this);

        for (var inputPinType : inputPins()) {
            var pin = VarPin.inputPin(inputPinType);
            node.addInputPin(pin, inputPinType.getName());
        }

        for (var outputPinType : outputPins()) {
            var pin = VarPin.outputPin(outputPinType);
            node.addOutputPin(pin, outputPinType.getName());
        }

        return node;
    }

    public ImageDrawable createIcon() {
        return iconSupplier.create(id);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public ActionType actionType() {
        return actionType;
    }

    public List<PinType> inputPins() {
        return inputPins;
    }

    public List<PinType> outputPins() {
        return outputPins;
    }

    public String category() {
        return category;
    }

    public Object extraData() {
        return extraData;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Action) obj;
        return Objects.equals(this.id, that.id) &&
                Objects.equals(this.name, that.name) &&
                Objects.equals(this.actionType, that.actionType) &&
                Objects.equals(this.inputPins, that.inputPins) &&
                Objects.equals(this.outputPins, that.outputPins) &&
                Objects.equals(this.category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, actionType, inputPins, outputPins, category);
    }

    @Override
    public String toString() {
        return "Action[" +
                "id=" + id + ", " +
                "name=" + name + ", " +
                "actionType=" + actionType + ", " +
                "inputPins=" + inputPins + ", " +
                "outputPins=" + outputPins + ", " +
                "extraData=" + extraData + ", " +
                "category=" + category + ']';
    }

}
