package com.rodev.jbpcore.blueprint.pin;

public interface LineDrawDispatcher {

    void drawTemporaryLineAt(int xStart, int yStart, int xEnd, int yEnd, int color);

    void clearLastTemporaryLine();

}
