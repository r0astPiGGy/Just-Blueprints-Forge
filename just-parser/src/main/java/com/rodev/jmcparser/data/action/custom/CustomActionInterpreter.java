package com.rodev.jmcparser.data.action.custom;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jmcparser.data.Interpreter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomActionInterpreter extends Interpreter<ActionEntity> {

    public CustomActionInterpreter(ActionEntity[] data) {
        super(data);
    }

    @Override
    protected @Nullable ActionEntity interpret(@NotNull ActionEntity object) {
        return object;
    }
}
