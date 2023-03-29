package com.rodev.jbpcore.utils;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.material.MaterialDrawable;
import icyllis.modernui.math.Rect;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static icyllis.modernui.view.View.dp;

public class MaterialButtonBackground extends MaterialDrawable {

    int padding = 0;
    int radius;

    public MaterialButtonBackground(int radius) {
        this.radius = radius;
    }

    @Override
    public void draw(@NotNull Canvas canvas) {
        var b = getBounds();
        var p = Paint.get();

        p.setColor(mColor);
        p.setAlpha(modulateAlpha(p.getAlpha(), mAlpha));

        if(p.getAlpha() == 0) return;

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
