package com.rodev.jbpcore.data.action;

import com.rodev.jbpcore.data.DataAccess;
import com.rodev.jbpcore.data.IconPathResolver;
import com.rodev.jbpcore.data.action.pin_type.PinType;
import com.rodev.jbpcore.data.action.type.ActionType;
import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.node.GraphNode;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.drawable.ImageDrawable;

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
    private final String iconNamespace;

    public Action(String id, String name, ActionType actionType, List<PinType> inputPins, List<PinType> outputPins,
                  String category, Object extraData, String iconNamespace) {
        this.id = id;
        this.name = name;
        this.actionType = actionType;
        this.inputPins = inputPins;
        this.outputPins = outputPins;
        this.category = category;
        this.extraData = extraData;
        this.iconNamespace = iconNamespace;

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
        return filter(acceptableOutputPins, outputType);
    }

    public boolean acceptsInputType(VariableType inputType) {
        return filter(acceptableInputPins, inputType);
    }

    private boolean filter(Set<VariableType> variableTypes, VariableType type) {
        return variableTypes.stream()
                .filter(type.getAcceptsFilter())
                .anyMatch(type.getDeclinesFilter());
    }

    public GraphNode toNode(Context context) {
        var node = actionType.createNode(context, this);

        for (var inputPinType : inputPins()) {
            var pin = inputPinType.createInputPin();
            node.addInputPin(pin, inputPinType.getName());
        }

        for (var outputPinType : outputPins()) {
            var pin = outputPinType.createOutputPin();
            node.addOutputPin(pin, outputPinType.getName());
        }

        node.resolveDynamicGroups();

        return node;
    }

    private String iconPathCache;

    public ImageDrawable createIcon() {
        if(iconPathCache == null) {
            iconPathCache = IconPathResolver.resolve(this);
        }

        return new ImageDrawable(DataAccess.TEXTURE_NAMESPACE, iconPathCache);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
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

    public String iconNamespace() {
        return iconNamespace;
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
                "outputPin=" + id + ", " +
                "name=" + name + ", " +
                "actionType=" + actionType + ", " +
                "inputPins=" + inputPins + ", " +
                "outputPins=" + outputPins + ", " +
                "extraData=" + extraData + ", " +
                "iconNamespace=" + iconNamespace + ", " +
                "category=" + category + ']';
    }

}
