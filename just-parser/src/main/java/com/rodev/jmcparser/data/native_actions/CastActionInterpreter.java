package com.rodev.jmcparser.data.native_actions;

import com.rodev.jbpcore.data.ObjectType;
import com.rodev.jbpcore.data.json.ActionEntity;
import com.rodev.jmcparser.data.Interpreter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class CastActionInterpreter extends Interpreter<ObjectType> {

    public CastActionInterpreter() {
        super(ObjectType.values());
    }

    @Override
    protected @Nullable ActionEntity interpret(@NotNull ObjectType object) {
        var actionEntity = new ActionEntity();
        var type = object.name().toLowerCase();
        var name = object.name;

        actionEntity.id = "native_cast_to_" + type;
        actionEntity.type = "native_cast";
        actionEntity.name = "Привести к типу " + name;
        actionEntity.category = "native_cast";
        actionEntity.icon_namespace = "native";
        actionEntity.input = createInput();
        actionEntity.output = createOutput(object);

        return actionEntity;
    }

    private List<ActionEntity.PinTypeEntity> createInput() {
        var list = new LinkedList<ActionEntity.PinTypeEntity>();
        var exec = createExec();
        var variable = new ActionEntity.PinTypeEntity();
        variable.type = "variable";
        variable.id = "var_to_cast";
        variable.label = "Переменная";

        list.add(exec);
        list.add(variable);

        return list;
    }

    private List<ActionEntity.PinTypeEntity> createOutput(ObjectType objectType) {
        var list = new LinkedList<ActionEntity.PinTypeEntity>();
        var castSuccess = createExec();
        var castFailed = createExec();

        castSuccess.id = "cast_success";
        castFailed.id = "cast_failed";
        castFailed.label = "Приведение не удалось";

        var variable = new ActionEntity.PinTypeEntity();
        variable.type = objectType.name().toLowerCase();
        variable.id = "casted_var";
        variable.label = objectType.name;

        list.add(castSuccess);
        list.add(variable);
        list.add(castFailed);

        return list;
    }
}
