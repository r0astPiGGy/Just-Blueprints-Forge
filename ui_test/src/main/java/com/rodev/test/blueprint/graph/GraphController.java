package com.rodev.test.blueprint.graph;

import com.rodev.test.blueprint.ContextActionType;
import icyllis.modernui.view.View;

public interface GraphController {

    View createViewAt(int x, int y, ContextActionType actionType);

    void navigateTo(int x, int y);

    void setViewMoveListener(ViewMoveListener viewMoveListener);

}
