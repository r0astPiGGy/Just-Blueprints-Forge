package com.rodev.jmcparser.data.action;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jmcparser.data.DataWriter;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class ActionWriter extends DataWriter<ActionEntity, ActionEntity> {

    public ActionWriter(File file) {
        super(file);
    }

    @Override
    public void write(ActionEntity[] data) {
        var list = Arrays.stream(data)
                .map(this::patch)
                .filter(Objects::nonNull)
                .toList();

        writeToFile(list);
    }
}
