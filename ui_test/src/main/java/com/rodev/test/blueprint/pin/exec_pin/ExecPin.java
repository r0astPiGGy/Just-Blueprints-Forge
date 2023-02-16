package com.rodev.test.blueprint.pin.exec_pin;

import com.rodev.test.Colors;
import com.rodev.test.blueprint.pin.Pin;
import com.rodev.test.blueprint.pin.var_pin.InVarPin;
import com.rodev.test.blueprint.pin.var_pin.OutVarPin;
import com.rodev.test.blueprint.pin.var_pin.VarPin;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Color;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.StateListDrawable;
import icyllis.modernui.material.MaterialDrawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.util.StateSet;

import javax.annotation.Nonnull;

import static icyllis.modernui.view.View.dp;
import static icyllis.modernui.widget.CompoundButton.CHECKED_STATE_SET;

public interface ExecPin extends Pin {

    @Override
    default Drawable createDrawable() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(CHECKED_STATE_SET, new CheckedDrawable());
        drawable.addState(StateSet.WILD_CARD, new UncheckedDrawable());
//        drawable.setEnterFadeDuration(300);
//        drawable.setExitFadeDuration(300);
        return drawable;
    }

    static Pin outputPin() {
        return new OutExecPin(Colors.WHITE);
    }

    static Pin inputPin() {
        return new InExecPin(Colors.WHITE);
    }

    class CheckedDrawable extends MaterialDrawable {

        CheckedDrawable() {}

        @Override
        public void draw(@Nonnull Canvas canvas) {
            final Rect r = getBounds();

            Paint paint = Paint.get();

            paint.setColor(mColor);
            paint.setAlpha(modulateAlpha(paint.getAlpha(), mAlpha));

            final var cX = r.centerX() + 5;
            final var cY = r.centerY();

            if (paint.getAlpha() != 0) {
                canvas.drawRect(r.left, r.top, r.left + cX, r.bottom, paint);
                canvas.drawTriangle(r.left + cX, r.top, r.right, r.top + cY, r.left + cX, r.bottom, paint);
            }
        }

        @Override
        public int getIntrinsicWidth() {
            // 24dp
            return dp(30);
        }

        @Override
        public int getIntrinsicHeight() {
            // 24dp
            return dp(24);
        }
    }

    class UncheckedDrawable extends MaterialDrawable {

        UncheckedDrawable() {}

        @Override
        public void draw(@Nonnull Canvas canvas) {
            final Rect r = getBounds();

            Paint paint = Paint.get();

            paint.setStyle(Paint.STROKE);
            paint.setColor(mColor);
            paint.setAlpha(modulateAlpha(paint.getAlpha(), mAlpha));

            final var cX = r.centerX() + 5;
            final var cY = r.centerY();

            if (paint.getAlpha() != 0) {
                canvas.drawRoundLine(r.left, r.top, r.left + cX, r.top, paint);
                canvas.drawRoundLine(r.left + cX, r.top, r.right, r.top + cY, paint);
                canvas.drawRoundLine(r.right, r.top + cY, r.left + cX, r.bottom, paint);
                canvas.drawRoundLine(r.left + cX, r.bottom, r.left, r.bottom, paint);
                canvas.drawRoundLine(r.left, r.bottom, r.left, r.top, paint);
            }
        }

        @Override
        public int getIntrinsicWidth() {
            // 24dp
            return dp(30);
        }

        @Override
        public int getIntrinsicHeight() {
            // 24dp
            return dp(24);
        }
    }
}
