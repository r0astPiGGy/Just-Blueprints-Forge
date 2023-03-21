package com.rodev.jmcparser.data;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jmcparser.generator.OnActionInterpretedListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    abstract
    protected @Nullable ActionEntity interpret(@NotNull T object);

}
