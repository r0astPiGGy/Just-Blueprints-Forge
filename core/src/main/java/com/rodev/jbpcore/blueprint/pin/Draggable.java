package com.rodev.jbpcore.blueprint.pin;

public interface Draggable<T> {

    void onDrag(int xStart, int yStart, int xEnd, int yEnd);

    void onDragEnd(int xStart, int yStart, int xEnd, int yEnd);

    void setDragListener(DragListener<T> dragListener);

}
