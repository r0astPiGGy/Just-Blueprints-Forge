package com.rodev.test.blueprint.data.action;

import com.rodev.test.blueprint.data.Registry;
import com.rodev.test.blueprint.data.action.type.ActionTypeRegistry;
import com.rodev.test.blueprint.data.json.ActionEntity;
import com.rodev.test.blueprint.data.variable.VariableTypeRegistry;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ActionRegistry extends Registry<String, Action> {

    private final ActionTypeRegistry actionTypeRegistry;
    private final VariableTypeRegistry variableTypeRegistry;

    public ActionRegistry(ActionTypeRegistry actionTypeRegistry, VariableTypeRegistry variableTypeRegistry) {
        this.actionTypeRegistry = actionTypeRegistry;
        this.variableTypeRegistry = variableTypeRegistry;
    }

    public void load(List<ActionEntity> actions){
        actions.stream().map(this::create).forEach(this::add);
    }

    private Action create(ActionEntity entity) {
        var actionType = actionTypeRegistry.get(entity.type);

        if(actionType == null) {
            throw new IllegalArgumentException("Unknown action type: " + entity.type);
        }

        var inputPins = create(entity.input);
        var outputPins = create(entity.output);

        return new Action(
                entity.id,
                entity.name,
                actionType,
                inputPins,
                outputPins,
                entity.category
        );
    }

    private List<PinType> create(List<ActionEntity.PinTypeEntity> pinTypes) {
        return new LinkedList<>(pinTypes.stream().map(this::create).toList());
    }

    private PinType create(ActionEntity.PinTypeEntity entity) {
        var varType = variableTypeRegistry.get(entity.type);

        if(varType == null) {
            throw new IllegalArgumentException("Unknown variable type: " + entity.type);
        }

        return new PinType(entity.id, entity.label, varType);
    }

    public Collection<Action> getAll() {
        return data.values();
    }

    private void add(Action action) {
        data.put(action.id(), action);
    }
}
