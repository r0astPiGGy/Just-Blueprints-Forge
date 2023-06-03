package com.rodev.jbpcore.blueprint.graph;

import com.rodev.jbpcore.ui.contextmenu.ContextMenuBuilder;
import icyllis.modernui.view.View;

public interface ContextMenuOpenHandler {

    ContextMenuBuilder createBuilder(View caller, float x, float y);

}
