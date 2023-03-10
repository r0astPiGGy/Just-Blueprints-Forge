package com.rodev.test.view;

import com.rodev.test.Colors;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.widget.Button;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static com.rodev.test.Fonts.MINECRAFT_FONT;

public class MaterialButton extends Button {

    private final Background background = new Background(Colors.NODE_BACKGROUND, dp(5));

    public MaterialButton() {
        setBackground(background);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setMinWidth(dp(140));

        setTypeface(MINECRAFT_FONT);
        setTextSize(sp(20));
    }

    public void setBackgroundColor(int color) {
        background.backgroundColor = color;
    }

    private static class Background extends Drawable {

        int padding = 0;
        int backgroundColor = Colors.NODE_BACKGROUND;
        int radius;

        public Background(int backgroundColor, int radius) {
            this.backgroundColor = backgroundColor;
            this.radius = radius;
        }

        @Override
        public void draw(@NotNull Canvas canvas) {
            var b = getBounds();
            var p = Paint.get();

            p.setColor(backgroundColor);
            canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, radius, p);
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
