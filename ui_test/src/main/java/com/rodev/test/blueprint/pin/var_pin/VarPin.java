package com.rodev.test.blueprint.pin.var_pin;

import com.rodev.test.blueprint.pin.*;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.StateListDrawable;
import icyllis.modernui.material.MaterialDrawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.util.StateSet;

import javax.annotation.Nonnull;

import static icyllis.modernui.view.View.dp;
import static icyllis.modernui.widget.CompoundButton.CHECKED_STATE_SET;

public interface VarPin extends Pin {

    @Override
    default Drawable createDrawable() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(CHECKED_STATE_SET, new CheckedDrawable());
        drawable.addState(StateSet.WILD_CARD, new UncheckedDrawable());
//        drawable.setEnterFadeDuration(300);
//        drawable.setExitFadeDuration(300);
        return drawable;
    }

    static Pin outputPin(int color) {
        return new OutVarPin(color);
    }

    static Pin inputPin(int color) {
        return new InVarPin(color);
    }

    class CheckedDrawable extends MaterialDrawable {

        private final float mRadius;

        CheckedDrawable() {
            mRadius = dp(4);
        }

        @Override
        public void draw(@Nonnull Canvas canvas) {
            final Rect r = getBounds();
            float cx = r.exactCenterX();
            float cy = r.exactCenterY();
            Paint paint = Paint.get();
            paint.setColor(mColor);
            paint.setAlpha(modulateAlpha(paint.getAlpha(), mAlpha));
            if (paint.getAlpha() != 0) {
                canvas.drawCircle(cx, cy, mRadius * 1.6f, paint);
                paint.setStyle(Paint.FILL_AND_STROKE);
                paint.setStrokeWidth(mRadius * 0.5f);
                canvas.drawCircle(cx, cy, mRadius * 1.6f, paint);
            }
        }

        @Override
        public int getIntrinsicWidth() {
            // 24dp
            return (int) (mRadius * 6);
        }

        @Override
        public int getIntrinsicHeight() {
            // 24dp
            return (int) (mRadius * 6);
        }
    }

    class UncheckedDrawable extends MaterialDrawable {

        private final float mRadius;

        UncheckedDrawable() {
            mRadius = dp(4);
        }

        @Override
        public void draw(@Nonnull Canvas canvas) {
            final Rect r = getBounds();
            float cx = r.exactCenterX();
            float cy = r.exactCenterY();
            Paint paint = Paint.get();
            paint.setColor(mColor);
            paint.setAlpha(modulateAlpha(paint.getAlpha(), mAlpha));
            if (paint.getAlpha() != 0) {
                paint.setStyle(Paint.STROKE);
                paint.setStrokeWidth(mRadius * 0.5f);
                canvas.drawCircle(cx, cy, mRadius * 1.6f, paint);
            }
        }

        @Override
        public int getIntrinsicWidth() {
            // 24dp
            return (int) (mRadius * 6);
        }

        @Override
        public int getIntrinsicHeight() {
            // 24dp
            return (int) (mRadius * 6);
        }
    }
}
