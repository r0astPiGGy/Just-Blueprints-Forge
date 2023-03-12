package com.rodev.jmcparser.data;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jbpcore.blueprint.data.json.VariableTypeEntity;

import java.io.File;
import java.util.*;

public class VariableTypeWriter {

    private final JsonDataWriter jsonDataWriter;

    public VariableTypeWriter(File fileToWrite) {
        jsonDataWriter = new JsonDataWriter(fileToWrite);
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
                .map(t -> new VariableTypeEntity(t, ""))
                .toList();

        jsonDataWriter.write(list);
    }

}
