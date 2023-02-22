package com.rodev.test.blueprint.graph;

import com.rodev.test.contextmenu.ContextMenuBuilder;

public interface ContextMenuBuilderProvider {

    ContextMenuBuilder createBuilder(float x, float y);

}
