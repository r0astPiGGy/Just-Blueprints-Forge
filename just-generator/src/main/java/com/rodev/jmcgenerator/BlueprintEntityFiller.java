package com.rodev.jmcgenerator;

import com.rodev.jmcgenerator.data.GeneratorData;
import com.rodev.jmcgenerator.entity.BlueprintEntity;
import com.rodev.jmcgenerator.entity.NodeEntity;
import com.rodev.jmcgenerator.entity.PinEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Log
public class BlueprintEntityFiller {

    private final GeneratorData generatorData;

    private final Map<String, PinEntity> pins = new HashMap<>();
    private final List<NodeEntity> events = new LinkedList<>();

    public List<NodeEntity> getEvents() {
        return events;
    }

    public void fill(BlueprintEntity blueprint) {
        for(var node : blueprint.nodes) {
            applyRepresentation(node);
            collectOutputPins(node);
        }

        for(var node : blueprint.nodes) {
            collectInputPins(node);
        }
    }

    private void applyRepresentation(NodeEntity entity) {
        var representation = generatorData.getById(entity.id);

        if(representation == null) {
            log.info("Representation data of the node by id '" + entity.id + "' not found! Skipping...");
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
