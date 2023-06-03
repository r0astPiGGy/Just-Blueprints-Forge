package com.rodev.jbpcore.blueprint.graph;

import com.rodev.jbpcore.blueprint.node.GraphNode;

public interface GraphController extends DrawListener, GraphTouchListener {

    void createNodeAt(int x, int y, GraphNode node);

    void onContextMenuOpen(int x, int y);

    void setViewMoveListener(ViewMoveListener viewMoveListener);

    void setViewHolder(ViewHolder viewHolder);

    void setContextMenuBuilderProvider(ContextMenuBuilderProvider provider);

}
