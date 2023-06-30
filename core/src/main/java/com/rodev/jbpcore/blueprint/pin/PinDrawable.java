package com.rodev.jbpcore.blueprint.pin;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.graphics.drawable.StateListDrawable;
import icyllis.modernui.util.StateSet;
import icyllis.modernui.view.View;

import static icyllis.modernui.widget.CompoundButton.CHECKED_STATE_SET;

public interface PinDrawable {

    default boolean shouldApplyButtonTintList() {
        return true;
    }

    static Drawable create(View contextHolder, Image disconnectedState, Image connectedState) {
        var drawable = new StateListDrawable(){
            @Override
            public int getIntrinsicHeight() {
                return contextHolder.dp(24);
            }

            @Override
            public int getIntrinsicWidth() {
                return contextHolder.dp(24);
            }
        };

        drawable.addState(CHECKED_STATE_SET, new ImageDrawable(connectedState));
        drawable.addState(StateSet.WILD_CARD, new ImageDrawable(disconnectedState));

        return drawable;
    }


}
