package com.rodev.test.blueprint.graph;

import com.rodev.test.blueprint.data.action.Action;
import icyllis.modernui.view.View;

public interface GraphController {

    View createViewAt(int x, int y, Action action);

    void navigateTo(int x, int y);

    void setViewMoveListener(ViewMoveListener viewMoveListener);

}
