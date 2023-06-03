package com.rodev.jbpcore.ui.drawable;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.View;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RoundedColoredBackground extends Drawable {

    private final int color;
    private final float radius;

    private final int paddingOffset;
    @Builder.Default
    private final DrawFunction drawFunction = (canvas, b, p, r) -> canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, r, p);

    @Override
    public void draw(@NotNull Canvas canvas) {
        var b = getBounds();
        var p = Paint.get();
        p.setColor(color);
        drawFunction.onDraw(canvas, b, p, radius);
    }

    @Override
    public boolean getPadding(@Nonnull Rect padding) {
        var r = calcPadding() + paddingOffset;
        padding.set(r, r, r, r);

        return true;
    }

    protected int calcPadding() {
        return (int) Math.ceil(radius / 2f);
    }

    public void applyTo(View view) {
        view.setBackground(this);
    }

    public interface DrawFunction {

        void onDraw(Canvas canvas, Rect bounds, Paint paint, float radius);

    }
}
