package com.rodev.jbpcore.blueprint.node;

public interface NodeMoveListener {

    boolean onMove(BPNode node, int xStart, int yStart, int xEnd, int yEnd);

}
