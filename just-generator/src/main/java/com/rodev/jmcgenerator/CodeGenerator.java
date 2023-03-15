package com.rodev.jmcgenerator;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.jmcgenerator.data.GeneratorData;
import com.rodev.jmcgenerator.entity.GeneratorEntity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

@RequiredArgsConstructor
@Log
public class CodeGenerator {

    private final File savedBlueprint;
    private final File fileToWrite;

    private GeneratorData generatorData;

    private final Map<String, Pin> pins = new HashMap<>();

    private final List<NodeEntity> events = new LinkedList<>();

    private static final Map<String, String> userInput = new HashMap<>();

    @SneakyThrows
    public void generate(GeneratorData data, int indentation) {
        this.generatorData = data;
        counter = 0;
        pins.clear();
        events.clear();
        userInput.clear();

        var objectMapper = new ObjectMapper();
        var a = objectMapper.readValue(savedBlueprint, BlueprintEntity.class);

        for(var node : a.nodes) {
            collectOutputPins(node);
            applyRepresentation(node);
        }

        for(var node : a.nodes) {
            collectInputPins(node);
        }

        var builder = new StringBuilder();

        for(var event : events) {
            var eventCode = getCodeForEventNode(event);
            builder.append(eventCode).append("\n");
        }

        var generatedCode = builder.toString();

        generatedCode = performIndentation(generatedCode, indentation);

        for(var pair : userInput.entrySet()) {
            generatedCode = generatedCode.replace(pair.getKey(), pair.getValue());
        }

        Files.writeString(fileToWrite.toPath(), generatedCode);
    }

    private String performIndentation(String code, final int spacing) {
        List<String> lines = new LinkedList<>();

        var linesSplit = code.split("\n");

        var space = 0;

        for(var line : linesSplit) {
            var currentSpace = space;

            if(line.contains("}")) {
                currentSpace -= spacing;
            }

            currentSpace = Math.max(0, currentSpace);

            lines.add(" ".repeat(currentSpace) + line);

            for(var ch : line.toCharArray()) {
                if(ch == '{') {
                    space += spacing;
                }
                if(ch == '}') {
                    space = Math.max(0, space - spacing);
                }
            }
        }

        return join(lines);
    }

    private String getCodeForEventNode(NodeEntity node) {
        var schema = node.getRawSchema();

        for(var outputExec : node.outputExecPins) {
            for(var con : outputExec.outputConnections) {
                var connectedNode = con.parent;
                var code = connectedNode.getCode();
                schema = schema.replace("$" + con.id, code);
            }
        }

        return schema;
    }

    // TODO: требует рефакторинг
    // присутствует баг с бесконечной рекурсией, когда у ноды аргумент является предыдущей подсоединенной нодой
    private static String getCodeForNode(@NotNull NodeEntity node) {
        var schema = node.getRawSchema();

        List<String> args = new LinkedList<>();

        for(var arg : node.arguments) {
            var argId = arg.id;
            var value = arg.value;

            boolean useUuid = true;

            if(arg.inputConnection != null) {
                value = getInput(arg.inputConnection);
                useUuid = false;
            }

            if (useUuid && value != null) {
                var uuid = UUID.randomUUID().toString();

                userInput.put(uuid, value);

                value = uuid;
            }

            if(value == null) {
                value = "";
            } else if(!node.representation.shouldIgnoreArgumentById(argId)){
                var tempValue = value;

                tempValue = argId + " = " + tempValue;

                args.add(tempValue);
            }

            schema = schema.replace("$" + argId, value);
        }

        for(var output : node.returns) {
            var value = getValueForPin(output);
            if(value != null) {
                schema = schema.replace("$" + output.id, value);
            }
        }

        var joined = String.join(", ", args);
        schema = schema.replace("$args", joined);

        for(var outputExec : node.outputExecPins) {
            var conId = "$" + outputExec.id;
            for(var con : outputExec.outputConnections) {
                var connectedNode = con.parent;
                var code = connectedNode.getCode();
                if(schema.contains(conId)) {
                    schema = schema.replace(conId, code);
                } else {
                    schema += "\n" + code;
                }
            }
            schema = schema.replace(conId, "");
        }

        return schema;
    }

    private static String getValueForPin(Pin pin) {
        var representation = pin.parent.representation;

        if(representation.output != null) {
            var output = representation.output.get(pin.id);

            if(output != null) {
                if(output.contains("$random_var_name")) {
                    output = output.replace("$random_var_name", getRandomVariableName());
                }

                representation.output.put(pin.id, output);
                pin.value = output;

                return output;
            }
        }

        return null;
    }

    private static String getInput(Pin pin) {
        var value = getValueForPin(pin);

        if(value != null) return value;

        return pin.parent.getCode();
    }

    private static int counter = 0;

    private static String getRandomVariableName() {
        counter++;

        return "generated_var" + counter;
    }

    private static String join(List<String> lines) {
        return String.join("\n", lines);
    }

    private void applyRepresentation(NodeEntity entity) {
        var representation = generatorData.getById(entity.id);

        if(representation == null) {
            log.warning("Representation data of the node by id '" + entity.id + "' not found! Skipping...");
            return;
        }

        if(representation.isEvent()) {
            events.add(entity);
        }

        entity.representation = representation;
    }

    private void collectOutputPins(NodeEntity parent) {
        if(parent.data.output == null) return;

        parent.data.output.forEach((id, pin) -> {
            collectPin(id, pin, parent);
            if(pin.isExecType()) {
                parent.outputExecPins.add(pin);
            } else {
                parent.returns.add(pin);
            }
        });
    }

    private void collectInputPins(NodeEntity parent) {
        if(parent.data.input == null) return;

        parent.data.input.forEach((id, pin) -> {
            collectPin(id, pin, parent);
            if(pin.isExecType()) {
                parent.inputExecPin = pin;
            } else {
                parent.arguments.add(pin);
            }
            if(pin.isConnected()) {
                var connectedId = pin.connectedTo;
                var output = pins.get(connectedId);

                if(output == null) {
                    log.warning("Output pin by id " + connectedId + " not found.");
                    return;
                }

                output.outputConnections.add(pin);
                pin.inputConnection = output;
            }
        });
    }

    private void collectPin(String id, Pin pin, NodeEntity parent) {
        pin.parent = parent;
        pins.put(id, pin);
    }

    public static class BlueprintEntity {
        public List<NodeEntity> nodes = new LinkedList<>();
    }

    public static class NodeEntity {
        public String id;
        public NodeData data;

        @JsonIgnore
        @Nullable
        public Object position;

        @JsonIgnore
        public GeneratorEntity representation;

        @JsonIgnore
        @Nullable
        public Pin inputExecPin;

        @JsonIgnore
        public final List<Pin> outputExecPins = new LinkedList<>();

        @JsonIgnore
        public final List<Pin> arguments = new LinkedList<>();

        @JsonIgnore
        public final List<Pin> returns = new LinkedList<>();

        @JsonIgnore
        private String cachedCode;

        @JsonIgnore
        public String getCode() {
            if(cachedCode == null) {
                cachedCode = getCodeForNode(this);
            }

            return cachedCode;
        }

        @JsonIgnore
        public String getRawSchema() {
            return join(representation.getSchemaLines());
        }
    }

    public static class NodeData {
        public Map<String, Pin> input;
        public Map<String, Pin> output;
    }

    public static class Pin {
        @JsonAlias("name")
        public String id;

        @Nullable
        public String connectedTo;

        @Nullable
        public String value;

        public String type;

        @JsonIgnore
        public NodeEntity parent;

        @JsonIgnore
        public final List<Pin> outputConnections = new LinkedList<>();

        @JsonIgnore
        public Pin inputConnection;

        @JsonIgnore
        public boolean isExecType() {
            return type.equals("exec");
        }

        @JsonIgnore
        public boolean isConnected() {
            return connectedTo != null;
        }
    }
}
