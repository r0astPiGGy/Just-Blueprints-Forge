package com.rodev.jmcgenerator.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class NodeEntity {
    public String id;
    @JsonAlias("data")
    public NodeData pins;

    @JsonIgnore
    @Nullable
    public Object position;

    @JsonIgnore
    public final GeneratorRelatedData data = new GeneratorRelatedData();

    @JsonIgnore
    public String getRawSchema() {
        return String.join("\n", data.representation.getSchemaLines());
    }

    @JsonIgnore
    public String getLocalId() {
        return data.localId.toString();
    }

    public static class GeneratorRelatedData {

        public final UUID localId = UUID.randomUUID();

        // Предыдущая подсоединенная нода
        @Nullable
        public PinEntity inputExecPin;

        // Следующие подсоединенные ноды
        public final List<PinEntity> outputExecPins = new LinkedList<>();

        // Аргументы ноды
        public final List<PinEntity> arguments = new LinkedList<>();

        // Возвращаемые значения ноды
        public final List<PinEntity> returns = new LinkedList<>();

        public GeneratorEntity representation;

    }
}
