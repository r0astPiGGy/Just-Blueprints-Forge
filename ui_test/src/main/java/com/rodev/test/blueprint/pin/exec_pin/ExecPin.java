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
import org.jetbrains.annotations.NotNull;

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

    abstract class ExecDrawable extends MaterialDrawable {

        protected final int heightOffset = 3;
        protected final int widthOffset = 3;

        ExecDrawable() {}

        @Override
        public int getIntrinsicWidth() {
            // 24dp
            return dp(24);
        }

        @Override
        public int getIntrinsicHeight() {
            // 24dp
            return dp(24);
        }

        @Override
        public void draw(@NotNull Canvas canvas) {
            final Rect r = getBounds().copy();

            Paint paint = Paint.get();

            paint.setStyle(Paint.STROKE);
            paint.setColor(mColor);
            paint.setAlpha(modulateAlpha(paint.getAlpha(), mAlpha));

            final var cX = r.centerX();
            final var cY = r.centerY();

            if (paint.getAlpha() != 0) {
                onDraw(canvas, r, paint, cX, cY);
            }
        }

        protected abstract void onDraw(Canvas canvas, Rect r, Paint paint, int cX, int cY);
    }

    class CheckedDrawable extends ExecDrawable {

        CheckedDrawable() {}

        @Override
        protected void onDraw(Canvas canvas, Rect r, Paint paint, int cX, int cY) {
            canvas.drawRect(
                    r.left + widthOffset,
                    r.top + heightOffset,
                    r.left + cX,
                    r.bottom - heightOffset,
                    paint
            );
            canvas.drawTriangle(
                    r.left + cX,
                    r.top + heightOffset,
                    r.right - widthOffset,
                    r.top + cY,
                    r.left + cX,
                    r.bottom - heightOffset,
                    paint
            );
        }
    }

    class UncheckedDrawable extends ExecDrawable {

        UncheckedDrawable() {}

        @Override
        protected void onDraw(Canvas canvas, Rect r, Paint paint, int cX, int cY) {
            canvas.drawRoundLine(
                    r.left + widthOffset,
                    r.top + heightOffset,
                    r.left + cX,
                    r.top + heightOffset,
                    paint
            );
            canvas.drawRoundLine(
                    r.left + cX,
                    r.top + heightOffset,
                    r.right - widthOffset,
                    r.top + cY,
                    paint
            );
            canvas.drawRoundLine(
                    r.right - widthOffset,
                    r.top + cY,
                    r.left + cX,
                    r.bottom - heightOffset,
                    paint
            );
            canvas.drawRoundLine(
                    r.left + cX,
                    r.bottom - heightOffset,
                    r.left + widthOffset,
                    r.bottom - heightOffset,
                    paint
            );
            canvas.drawRoundLine(
                    r.left + widthOffset,
                    r.bottom - heightOffset,
                    r.left + widthOffset,
                    r.top + heightOffset,
                    paint
            );
        }
    }
}