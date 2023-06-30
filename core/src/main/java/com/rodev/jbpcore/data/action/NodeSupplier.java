package com.rodev.jbpcore.data.action;

import com.rodev.jbpcore.blueprint.node.GraphNode;
import com.rodev.jbpcore.ui.node.NodeView;
import icyllis.modernui.core.Context;

public interface NodeSupplier {

    NodeSupplier identity = (context, color, action) -> {
        return new NodeView(context, color, action.id(), action.name(), action.createIcon());
    };

    GraphNode create(Context context, int color, Action action);

}
