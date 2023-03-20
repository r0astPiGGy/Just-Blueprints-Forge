package com.rodev.jbpcore.blueprint.data.action;

import com.rodev.jbpcore.blueprint.node.BPNode;
import com.rodev.jbpcore.blueprint.node.impl.NodeView;
import com.rodev.jbpcore.blueprint.pin.exec_pin.ExecPin;

public interface NodeSupplier {

    NodeSupplier identity = (color, action) -> {
        return new NodeView(color, action.id(), action.name(), action.createIcon());
    };

    BPNode create(int color, Action action);

}
