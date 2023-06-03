package com.rodev.jmcparser.data.action.custom;

import com.rodev.jbpcore.data.json.ActionEntity;
import com.rodev.jmcparser.data.Interpreter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomActionInterpreter extends Interpreter<CustomActionEntity> {

    public CustomActionInterpreter(CustomActionEntity[] data) {
        super(data);
    }

    @Override
    protected @Nullable ActionEntity interpret(@NotNull CustomActionEntity object) {
        return new ActionEntity(
                object.id,
                object.type,
                object.name,
                object.category,
                object.input,
                object.output,
                object.extra_data,
                object.icon_namespace
        );
    }
}
