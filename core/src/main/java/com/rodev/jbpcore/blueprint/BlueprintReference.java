package com.rodev.jbpcore.blueprint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.jbpcore.blueprint.node.GraphNode;
import com.rodev.jbpcore.blueprint.node.PinConnection;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.data.DataAccess;
import com.rodev.jbpcore.data.action.Action;
import com.rodev.jbpcore.workspace.impl.WorkspaceImpl;
import icyllis.modernui.core.Context;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Log4j2
public record BlueprintReference(File file) {

    public Blueprint load(Context context) {
        if (!file.exists()) {

            return new Blueprint(file, new BlueprintDto(Collections.emptyMap(), Collections.emptyList(), Collections.emptyList()));
        }

        var objectMapper = new ObjectMapper();

        BlueprintEntity blueprint;

        try {
            blueprint = objectMapper.readValue(file, BlueprintEntity.class);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }

        Map<String, Pin> outputPins = new HashMap<>();
        List<PinConnection> connections = new LinkedList<>();
        List<NodeLocationWrapper> nodes = new LinkedList<>();

        for (var nodeEntity : blueprint.nodes) {
            var actionId = nodeEntity.id;

            // TODO refactor: (static dependency)
            Action action = DataAccess.getInstance().actionRegistry.get(actionId);

            if (action == null) {
                log.warn("Action by id " + actionId + " not found during blueprint load. (Outdated blueprint?)");
                continue;
            }

            GraphNode node = action.toNode(context);

            var deserializer = node.getDeserializer(nodeEntity.data);
            deserializer.deserialize();

            outputPins.putAll(deserializer.getOutputPins());
            connections.addAll(deserializer.getPinConnections());

            int x = nodeEntity.position.x;
            int y = nodeEntity.position.y;

            nodes.add(new NodeLocationWrapper(x, y, node));
        }

        var blueprintDto = new BlueprintDto(outputPins, connections, nodes);

        return new Blueprint(file, blueprintDto);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class BlueprintEntity {
        public List<NodeEntity> nodes;
    }

    public static class NodeEntity {
        public String id;
        public NodeLocation position;
        public Object data;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class NodeLocation {
        public int x, y;
    }
}
