package com.rodev.jbpcore.blueprint.graph;

import com.rodev.jbpcore.blueprint.node.BPNode;
import com.rodev.jbpcore.blueprint.pin.Pin;

public interface GraphController extends DrawListener, GraphTouchListener {

    void createNodeAt(int x, int y, BPNode node);

    void onContextMenuOpen(int x, int y);

    void setViewMoveListener(ViewMoveListener viewMoveListener);

    void connect(Pin inputPin, Pin outputPin);

    void setViewHolder(ViewHolder viewHolder);

    void setContextMenuBuilderProvider(ContextMenuBuilderProvider provider);

}
