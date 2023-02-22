package com.rodev.test.blueprint.graph;

import com.rodev.test.blueprint.data.action.Action;
import com.rodev.test.contextmenu.ContextMenuBuilder;
import icyllis.modernui.view.View;

public interface GraphController extends DrawListener, GraphTouchListener {

    void onContextMenuOpen(int x, int y);

    void setViewMoveListener(ViewMoveListener viewMoveListener);

    void setViewHolder(ViewHolder viewHolder);

    void setContextMenuBuilderProvider(ContextMenuBuilderProvider provider);

}
