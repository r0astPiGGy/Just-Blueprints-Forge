package com.rodev.jbpcore.ui.node;

import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;

public class SpaceHelperView extends View {

    public SpaceHelperView() {
        setLayoutParams(new ViewGroup.LayoutParams(0, 0));
    }

    public void onLastMeasure(int leftViewWidth, int rightViewWidth, int headerWidth) {
        var viewsWidth = leftViewWidth + rightViewWidth;

        getLayoutParams().width = Math.max(0, headerWidth - viewsWidth);

        requestLayout();
    }

}
