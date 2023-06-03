package com.rodev.jbpcore.blueprint.pin;

public interface DragListener<T> {

    void onDrag(T obj, int xStart, int yStart, int xEnd, int yEnd);

    void onDragEnded(T obj, int xStart, int yStart, int xEnd, int yEnd);

}
