package com.rodev.jbpcore.blueprint;

import com.rodev.jbpcore.blueprint.graph.GraphController;
import com.rodev.jbpcore.blueprint.node.PinConnection;
import com.rodev.jbpcore.blueprint.pin.Pin;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class BlueprintDto {

    private final Map<String, Pin> outputPins;
    private final List<PinConnection> connections;
    private final List<NodeLocationWrapper> nodes;

    public void loadInto(GraphController graphController) {
        for (NodeLocationWrapper wrapper : nodes) {
            graphController.createNodeAt(wrapper.x(), wrapper.y(), wrapper.node());
        }

        for (var connection : connections) {
            var outputPinId = connection.outputPin();
            var outputPin = outputPins.get(outputPinId);

            connection.inputPin().connect(outputPin);
        }
    }

}
