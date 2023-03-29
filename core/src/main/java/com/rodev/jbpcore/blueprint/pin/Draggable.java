package com.rodev.jbpcore.blueprint.pin;

public interface Draggable {

    void onLineDraw(int xStart, int yStart, int xEnd, int yEnd);

    void onLineDrawEnd(int xStart, int yStart, int xEnd, int yEnd);

    void setPinDragListener(PinDragListener pinDragListener);

}
