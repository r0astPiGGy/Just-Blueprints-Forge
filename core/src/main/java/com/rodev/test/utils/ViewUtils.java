package com.rodev.test.utils;

import com.rodev.test.blueprint.ChildRoot;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;

public class ViewUtils {

    public static int[] getRelativeViewPosition(View view) {
        var parent = view.getParent();
        while(!(parent instanceof ChildRoot childRoot)) {
            if(parent == null) return null;

            parent = parent.getParent();
        }

        return childRoot.getChildCoordinates(view);
    }

    public static LinearLayout.LayoutParams squaredSize(int sideLength, View view) {
        var params = new LinearLayout.LayoutParams(sideLength, sideLength);

        view.setLayoutParams(params);

        return params;
    }

    public static ViewGroup.LayoutParams wrapContent(View view) {
        var params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        view.setLayoutParams(params);

        return params;
    }

    public static ViewGroup.LayoutParams matchParent(View view) {
        var params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        view.setLayoutParams(params);

        return params;
    }

    public static ViewGroup.LayoutParams wrapContentMatchParent(View view) {
        var params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        view.setLayoutParams(params);

        return params;
    }

    public static ViewGroup.LayoutParams matchParentWrapContent(View view) {
        var params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        view.setLayoutParams(params);

        return params;
    }


}
