package com.rodev.jbpcore.blueprint.pin;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.graphics.drawable.StateListDrawable;
import icyllis.modernui.util.StateSet;
import org.jetbrains.annotations.NotNull;

import static icyllis.modernui.view.View.dp;
import static icyllis.modernui.widget.CompoundButton.CHECKED_STATE_SET;

public interface PinDrawable {

    default boolean shouldApplyButtonTintList() {
        return true;
    }

    static Impl create(Image disconnectedState, Image connectedState) {
        var drawable = new Impl();

        drawable.addState(CHECKED_STATE_SET, new ImageDrawable(connectedState));
        drawable.addState(StateSet.WILD_CARD, new ImageDrawable(disconnectedState));

        return drawable;
    }

    class Impl extends StateListDrawable implements PinDrawable {
        @Override
        public int getIntrinsicHeight() {
            return dp(24);
        }

        @Override
        public int getIntrinsicWidth() {
            return dp(24);
        }
    }


}
