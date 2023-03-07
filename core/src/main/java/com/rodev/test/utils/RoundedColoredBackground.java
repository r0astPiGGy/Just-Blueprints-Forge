package com.rodev.test.utils;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.view.View;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RoundedColoredBackground extends Drawable {

    private final int color;
    private final float radius;

    @Override
    public void draw(@NotNull Canvas canvas) {
        var b = getBounds();
        var p = Paint.get();
        p.setColor(color);
        canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, radius, p);
    }

    public static RoundedColoredBackground of(int color, float radius) {
        return new RoundedColoredBackground(color, radius);
    }

    public void applyTo(View view) {
        view.setBackground(this);
    }
}
