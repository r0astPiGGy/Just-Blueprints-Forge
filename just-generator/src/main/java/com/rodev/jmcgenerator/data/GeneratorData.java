package com.rodev.jmcgenerator.data;

import com.rodev.jmcgenerator.entity.GeneratorEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GeneratorData {

    private final Map<String, GeneratorEntity> data = new HashMap<>();

    public void load(GeneratorEntity[] entities) {
        Arrays.stream(entities).forEach(e -> data.put(e.id, e));
    }

    @Nullable
    public GeneratorEntity getById(String nodeId) {
        return data.get(nodeId);
    }

}
