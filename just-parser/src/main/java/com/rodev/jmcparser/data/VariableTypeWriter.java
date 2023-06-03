package com.rodev.jmcparser.data;

import com.rodev.jbpcore.data.json.ActionEntity;
import com.rodev.jbpcore.data.json.VariableTypeEntity;

import java.io.File;
import java.util.*;

public class VariableTypeWriter extends DataWriter<ActionEntity, VariableTypeEntity> {

    public VariableTypeWriter(File file) {
        super(file);
    }

    public void write(ActionEntity[] entities) {
        Set<String> types = new HashSet<>();

        for(var ent : entities) {
            for(var pin : ent.input) {
                types.add(pin.type);
            }
            for(var pin : ent.output) {
                types.add(pin.type);
            }
        }

        var list = types.stream()
                .filter(Objects::nonNull)
                .map(t -> new VariableTypeEntity(t, "0, 0, 0", "default"))
                .map(this::patch)
                .filter(Objects::nonNull)
                .toList();

        writeToFile(list);
    }

}
