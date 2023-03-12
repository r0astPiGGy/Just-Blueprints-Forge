package com.rodev.jbpcore.blueprint.graph;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;

public interface DrawListener {

    void onDraw(Canvas canvas, Paint paint);

    default void setInvalidationCallback(Runnable invalidationCallback) {}

}
