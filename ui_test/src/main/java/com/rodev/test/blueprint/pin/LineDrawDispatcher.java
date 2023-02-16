package com.rodev.test.blueprint.pin;

public interface LineDrawDispatcher {

    void drawTemporaryLineAt(int xStart, int yStart, int xEnd, int yEnd, int color);

    void clearLastTemporaryLine();

}
