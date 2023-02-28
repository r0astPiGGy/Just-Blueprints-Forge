package com.rodev.test.blueprint.data.action;

import com.rodev.test.blueprint.data.Registry;
import com.rodev.test.blueprint.data.action.type.ActionTypeRegistry;
import com.rodev.test.blueprint.data.json.ActionEntity;
import com.rodev.test.blueprint.data.variable.VariableTypeRegistry;

import java.util.*;

public class ActionRegistry extends Registry<String, Action> {

    private final ActionTypeRegistry actionTypeRegistry;
    private final VariableTypeRegistry variableTypeRegistry;
    private final Map<String, PinTypeFactory> pinTypeFactoryMap = new HashMap<>();

    private static final PinTypeFactory defaultPinTypeFactory =
            (entity, type) -> new PinType(entity.id, entity.label, type);

    public ActionRegistry(ActionTypeRegistry actionTypeRegistry, VariableTypeRegistry variableTypeRegistry) {
        this.actionTypeRegistry = actionTypeRegistry;
        this.variableTypeRegistry = variableTypeRegistry;
    }

    public void load(ActionEntity[] actions){
        Arrays.stream(actions).map(this::create).forEach(this::add);
    }

    public void registerPinTypeFactory(String typeId, PinTypeFactory factory) {
        pinTypeFactoryMap.put(typeId, factory);
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

        var factory = pinTypeFactoryMap.getOrDefault(varType.type(), defaultPinTypeFactory);

        return factory.create(entity, varType);
    }

    public Collection<Action> getAll() {
        return data.values();
    }

    private void add(Action action) {
        data.put(action.id(), action);
    }
}
