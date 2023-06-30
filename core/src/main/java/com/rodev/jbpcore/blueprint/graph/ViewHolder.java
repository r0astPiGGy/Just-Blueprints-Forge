package com.rodev.jbpcore.blueprint.graph;

import icyllis.modernui.core.Context;
import icyllis.modernui.view.View;

public interface ViewHolder {

    void addViewAt(View view, int x, int y);

    Context provideContext();

}
