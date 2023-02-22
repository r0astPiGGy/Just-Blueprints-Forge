package com.rodev.test.blueprint.graph;

import com.rodev.test.contextmenu.ContextMenuBuilder;
import icyllis.modernui.view.View;

public interface ContextMenuOpenHandler {

    ContextMenuBuilder createBuilder(View caller, float x, float y);

}
