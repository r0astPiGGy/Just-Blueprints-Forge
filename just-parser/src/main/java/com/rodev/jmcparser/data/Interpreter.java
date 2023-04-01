package com.rodev.jmcparser.data;

import com.rodev.jbpcore.blueprint.data.action.Action;
import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jmcparser.generator.OnActionInterpretedListener;
import com.rodev.jmcparser.json.GameValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class Interpreter<T> {

    private final List<OnActionInterpretedListener<T>> listeners = new LinkedList<>();

    private final T[] data;

    protected Interpreter(T[] data) {
        this.data = data;
    }

    public void addOnActionInterpretedListener(OnActionInterpretedListener<T> listener) {
        listeners.add(listener);
    }

    protected void notifyListeners(ActionEntity entity, T data) {
        listeners.forEach(l -> l.accept(entity, data));
    }

    public ActionEntity[] interpret() {
        var actions = new ActionEntity[data.length];

        for(int i = 0; i < data.length; i++) {
            var object = data[i];
            if(object == null) continue;

            var action = interpret(data[i]);

            if(action == null) continue;

            actions[i] = action;

            notifyListeners(action, object);
        }

        return actions;
    }

    public ActionEntity.PinTypeEntity createExec() {
        var exec = new ActionEntity.PinTypeEntity();
        exec.id = "exec";
        exec.type = "exec";
        exec.label = "";

        return exec;
    }

    public void applyForList(ActionEntity.PinTypeEntity list, String valueType) {
        list.extra_data = new HashMap<>() {{
            put("element-type", anyToDynamic(valueType));
        }};
    }

    public void applyForMap(ActionEntity.PinTypeEntity map, String keyType, String valueType) {
        map.extra_data = new HashMap<>() {{
            put("key-type", anyToDynamic(keyType));
            put("value-type", anyToDynamic(valueType));
        }};
    }

    public String anyToDynamic(String type) {
        if(type.equals("any")) return "dynamic";

        return type;
    }

    public void applyExtraDataToGameValue(ActionEntity.PinTypeEntity entity, GameValue gameValue) {
        if(entity.type.equals("list")) {
            applyForList(entity, gameValue.elementType);
        }
        if(entity.type.equals("dictionary")) {
            applyForMap(entity, gameValue.keyType, gameValue.valueType);
        }
    }

    abstract
    protected @Nullable ActionEntity interpret(@NotNull T object);

}
