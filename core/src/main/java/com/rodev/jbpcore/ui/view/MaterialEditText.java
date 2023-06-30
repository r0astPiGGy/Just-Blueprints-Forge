package com.rodev.jbpcore.ui.view;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.utils.ViewUtils;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.widget.EditText;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class MaterialEditText extends EditText {

    private final Background background = new Background(Colors.WHITE, Colors.NODE_BACKGROUND, dp(2));

    public MaterialEditText(Context context) {
        super(context);
        setBackground(background);
    }

    public void setOutlineColor(int color) {
        background.outlineColor = color;
        invalidateDrawable(background);
    }

    public void setBackgroundColor(int color) {
        background.backgroundColor = color;
        invalidateDrawable(background);
    }

    private class Background extends Drawable {

        int padding = 0;
        int outlineColor = Colors.WHITE;
        int backgroundColor = Colors.NODE_BACKGROUND;
        int radius;

        public Background(int outlineColor, int backgroundColor, int radius) {
            this.outlineColor = outlineColor;
            this.backgroundColor = backgroundColor;
            this.radius = radius;
        }

        @Override
        public void draw(@NotNull Canvas canvas) {
            var b = getBounds();
            var p = Paint.obtain();

            p.setColor(outlineColor);
            canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, radius, p);

            p.setColor(backgroundColor);

            var tempPadding = ((float) padding) / 1.5f;

            canvas.drawRoundRect(
                    b.left + tempPadding,
                    b.top + tempPadding,
                    b.right - tempPadding,
                    b.bottom - tempPadding,
                    ((float) (radius) / 2) * 1.5f,
                    p
            );
        }

        @Override
        public boolean getPadding(@Nonnull Rect padding) {
            var r = calcPadding() + dp(5);
            padding.set(r, r, r, r);

            return true;
        }

        protected int calcPadding() {
            return this.padding = (int) Math.ceil(radius / 2f);
        }
    }

}
