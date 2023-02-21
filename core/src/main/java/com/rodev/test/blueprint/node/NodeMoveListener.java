package com.rodev.test.blueprint.node;

import icyllis.modernui.view.View;

public interface NodeMoveListener {

    <T extends View & BPNode> boolean onMove(T node, int xStart, int yStart, int xEnd, int yEnd);

}
