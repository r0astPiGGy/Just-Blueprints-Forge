package com.rodev.jbpcore.ui.drawable;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.material.MaterialDrawable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import java.util.function.Function;
import java.util.function.Supplier;

import static icyllis.modernui.graphics.MathUtil.modulateAlpha;

public class MaterialButtonBackground extends MaterialDrawable {

    private final Function<Float, Integer> dpSupplier;
    int padding = 0;
    int radius;

    public MaterialButtonBackground(Function<Float, Integer> dpSupplier, int radius) {
        this.radius = radius;
        this.dpSupplier = dpSupplier;
    }

    @Override
    public void draw(@NotNull Canvas canvas) {
        var b = getBounds();
        var p = Paint.obtain();

        p.setColor(mColor);
        p.setAlpha(modulateAlpha(p.getAlpha(), mAlpha));

        if(p.getAlpha() == 0) return;

        canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, radius, p);
    }

    @Override
    public boolean getPadding(@Nonnull Rect padding) {
        var r = calcPadding() + dpSupplier.apply(5f);
        padding.set(r, r, r, r);

        return true;
    }

    protected int calcPadding() {
        return this.padding = (int) Math.ceil(radius / 2f);
    }
}
