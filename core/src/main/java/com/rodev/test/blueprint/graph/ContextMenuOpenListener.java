package com.rodev.test.blueprint.graph;

import com.rodev.test.blueprint.data.action.Action;
import icyllis.modernui.view.View;

import java.util.function.Consumer;

public interface ContextMenuOpenListener {

    void onContextMenuOpen(Consumer<Action> onItemClick, View caller, float x, float y);

}
