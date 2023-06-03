package com.rodev.jbpcore.data.action;

import com.rodev.jbpcore.blueprint.node.GraphNode;
import com.rodev.jbpcore.ui.node.NodeView;

public interface NodeSupplier {

    NodeSupplier identity = (color, action) -> {
        return new NodeView(color, action.id(), action.name(), action.createIcon());
    };

    GraphNode create(int color, Action action);

}
