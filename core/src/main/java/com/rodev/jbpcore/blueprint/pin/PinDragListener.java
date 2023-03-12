package com.rodev.jbpcore.blueprint.pin;

public interface PinDragListener {

    void onLineDrag(Pin pin, int xStart, int yStart, int xEnd, int yEnd);

    void onLineDragEnded(Pin pin, int xStart, int yStart, int xEnd, int yEnd);

}
