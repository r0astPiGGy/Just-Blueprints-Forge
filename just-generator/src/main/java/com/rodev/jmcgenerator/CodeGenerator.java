package com.rodev.jmcgenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.jmcgenerator.data.GeneratorData;
import com.rodev.jmcgenerator.entity.BlueprintEntity;
import com.rodev.jmcgenerator.entity.NodeEntity;
import com.rodev.jmcgenerator.entity.PinEntity;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.io.File;
import java.nio.file.Files;
import java.util.*;

@RequiredArgsConstructor
@Log
public class CodeGenerator {

    private final File savedBlueprint;
    private final File fileToWrite;

    private GeneratorData generatorData;

    private final Map<String, PinEntity> pins = new HashMap<>();

    private final List<NodeEntity> events = new LinkedList<>();

    private NodeFillerHelper nodeFillerHelper;

    @SneakyThrows
    public void generate(GeneratorData data, int indentation) {
        this.generatorData = data;
        pins.clear();
        events.clear();

        nodeFillerHelper = new NodeFillerHelper();

        var objectMapper = new ObjectMapper();
        var blueprint = objectMapper.readValue(savedBlueprint, BlueprintEntity.class);

        for(var node : blueprint.nodes) {
            collectOutputPins(node);
            applyRepresentation(node);
        }

        for(var node : blueprint.nodes) {
            collectInputPins(node);
        }

        var builder = new StringBuilder();

        for(var event : events) {
            var eventCode = getCodeForEventNode(event);
            builder.append(eventCode).append("\n");
        }

        var generatedCode = builder.toString();

        generatedCode = CodeIndentation.applyIndentation(generatedCode, indentation);
        generatedCode = nodeFillerHelper.replaceUserInputIn(generatedCode);

        Files.writeString(fileToWrite.toPath(), generatedCode);
    }

    private String getCodeForEventNode(NodeEntity node) {
        var schema = node.getRawSchema();

        for(var outputExec : node.data.outputExecPins) {
            for(var con : outputExec.data.getConnections()) {
                var connectedNode = con.data.parent;
                var code = nodeFillerHelper.getCode(connectedNode);
                schema = schema.replace("$" + con.id, code);
            }
            schema = schema.replace("$" + outputExec.id, "");
        }

        return schema;
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

        entity.data.representation = representation;
    }

    private void collectOutputPins(NodeEntity parent) {
        if(parent.pins.output == null) return;

        parent.pins.output.forEach((id, pin) -> {
            pin.data.setTypeOfOutput();

            collectPin(id, pin, parent);
            if(pin.isExecType()) {
                parent.data.outputExecPins.add(pin);
            } else {
                parent.data.returns.add(pin);
            }
        });
    }

    private void collectInputPins(NodeEntity parent) {
        if(parent.pins.input == null) return;

        parent.pins.input.forEach((id, pin) -> {
            collectPin(id, pin, parent);

            pin.data.setTypeOfInput();

            if(pin.isExecType()) {
                parent.data.inputExecPin = pin;
            } else {
                parent.data.arguments.add(pin);
            }
            if(!pin.isConnected()) return;

            var connectedId = pin.connectedTo;
            var output = pins.get(connectedId);

            if(output == null) {
                log.warning("Output pin by id " + connectedId + " not found.");
                return;
            }

            output.data.addConnection(pin);
            pin.data.setConnection(output);
        });
    }

    private void collectPin(String id, PinEntity pin, NodeEntity parent) {
        pin.data.parent = parent;
        pins.put(id, pin);
    }

}
