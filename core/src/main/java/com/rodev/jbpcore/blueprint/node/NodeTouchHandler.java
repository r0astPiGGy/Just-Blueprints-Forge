package com.rodev.jbpcore.blueprint.node;

import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;

public class NodeTouchHandler<T extends View & GraphNode> {

    private NodeMoveListener nodeMoveListener;
    private NodeTouchListener nodeTouchListener;

    private final T node;

    private int lastTouchX, lastTouchY;

    public NodeTouchHandler(T node) {
        this.node = node;
    }

    public void setNodeTouchListener(NodeTouchListener nodeTouchListener) {
        this.nodeTouchListener = nodeTouchListener;
    }

    public void setNodeMoveListener(NodeMoveListener nodeMoveListener) {
        this.nodeMoveListener = nodeMoveListener;
    }

    public boolean handle(MotionEvent event) {
        if(event.getButtonState() != MotionEvent.BUTTON_PRIMARY) return false;

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            lastTouchX = (int) event.getX();
            lastTouchY = (int) event.getY();
            if(nodeTouchListener != null) {
                nodeTouchListener.onNodeTouch(node, lastTouchX, lastTouchY);
                return true;
            }
        }

        if(nodeMoveListener != null && event.getAction() == MotionEvent.ACTION_MOVE) {
            int x = node.getLeft();
            int y = node.getTop();

            int newX = x + (int) event.getX() - lastTouchX;
            int newY = y + (int) event.getY() - lastTouchY;

            nodeMoveListener.onMove(node, x, y, newX, newY);

            return true;
        }


        return false;
    }
}
