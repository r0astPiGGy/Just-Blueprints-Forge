package com.rodev.jmcparser.data;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jbpcore.blueprint.data.json.ActionTypeEntity;
import com.rodev.jbpcore.blueprint.data.json.VariableTypeEntity;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ActionTypeWriter extends DataWriter<ActionEntity, ActionTypeEntity> {

    public ActionTypeWriter(File file) {
        super(file);
    }

    @Override
    public void write(ActionEntity[] data) {
        Set<String> types = new HashSet<>();

        for(var ent : data) {
            types.add(ent.type);
        }

        var list = types.stream()
                .filter(Objects::nonNull)
                .map(t -> new ActionTypeEntity(t, ""))
                .map(this::patch)
                .filter(Objects::nonNull)
                .toList();

        writeToFile(list);
    }
}
