package com.rodev.jbpcore.blueprint;

import icyllis.modernui.view.View;

public interface ChildRoot {

    int[] getChildCoordinates(View view);

    static int[] getChildCoordinates(View childRoot, View view) {
        int originX = childRoot.getLeft() + view.getLeft() + (view.getWidth() / 2);
        int originY = childRoot.getTop() + view.getTop() + (view.getHeight() / 2);

        var parent = (View) view.getParent();
        while(parent != childRoot) {
            if(parent == null) return null;

            originX += parent.getLeft();
            originY += parent.getTop();

            parent = (View) parent.getParent();
        }

        return new int[] { originX, originY };
    }

}
