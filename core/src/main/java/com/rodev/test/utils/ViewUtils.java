package com.rodev.test.utils;

import com.rodev.test.blueprint.ChildRoot;
import icyllis.modernui.view.View;

public class ViewUtils {

    public static int[] getRelativeViewPosition(View view) {
        var parent = view.getParent();
        while(!(parent instanceof ChildRoot childRoot)) {
            if(parent == null) return null;

            parent = parent.getParent();
        }

        return childRoot.getChildCoordinates(view);
    }


}
