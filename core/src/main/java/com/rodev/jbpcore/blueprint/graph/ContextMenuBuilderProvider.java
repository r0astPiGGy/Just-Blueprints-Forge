package com.rodev.jbpcore.blueprint.graph;

import com.rodev.jbpcore.contextmenu.ContextMenuBuilder;

public interface ContextMenuBuilderProvider {

    ContextMenuBuilder createBuilder(float x, float y);

}
