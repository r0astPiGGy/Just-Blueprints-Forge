package com.rodev.test.blueprint.graph;

import com.rodev.test.blueprint.node.BPNode;
import com.rodev.test.blueprint.pin.Pin;

public interface GraphController extends DrawListener, GraphTouchListener {

    void createNodeAt(int x, int y, BPNode node);

    void onContextMenuOpen(int x, int y);

    void setViewMoveListener(ViewMoveListener viewMoveListener);

    void connect(Pin inputPin, Pin outputPin);

    void setViewHolder(ViewHolder viewHolder);

    void setContextMenuBuilderProvider(ContextMenuBuilderProvider provider);

}
