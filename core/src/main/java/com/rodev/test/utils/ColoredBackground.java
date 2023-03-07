package com.rodev.test.utils;

import com.rodev.test.fragment.welcome.WelcomeScreenFragment;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.View;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ColoredBackground extends Drawable {

    private final int color;

    private DrawFunction drawFunction = Canvas::drawRect;

    @Override
    public void draw(@NotNull Canvas canvas) {
        var b = getBounds();
        var p = Paint.get();
        p.setColor(color);
        drawFunction.onDraw(canvas, b, p);
    }

    public static ColoredBackground of(int color) {
        return new ColoredBackground(color);
    }

    public void applyTo(View view) {
        view.setBackground(this);
    }

    public ColoredBackground setDrawFunction(DrawFunction func) {
        drawFunction = func;
        return this;
    }

    public interface DrawFunction {

        void onDraw(Canvas canvas, Rect bounds, Paint paint);

    }
}
