package com.rodev.test.blueprint.node.impl;

import com.rodev.test.blueprint.node.SelectableDrawable;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.view.Gravity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class NodeDrawable extends SelectableDrawable {

    private final Supplier<Integer> headerHeightSupplier;
    private final int[] headerColors;

    public NodeDrawable(Supplier<Integer> headerHeightSupplier, int headerColor) {
        super();
        this.headerHeightSupplier = headerHeightSupplier;
        headerColors = new int[]{headerColor, backgroundColor, backgroundColor, headerColor};
    }

    @Override
    public void draw(@NotNull Canvas canvas) {
        super.draw(canvas);

        var b = getBounds();
        var paint = Paint.take();

        int headerHeight = headerHeightSupplier.get() + padding;

        paint.setColors(headerColors);
        canvas.drawRoundRect(
                b.left + selectionOffset,
                b.top + selectionOffset,
                b.right - selectionOffset,
                b.top + headerHeight,
                mRadius,
                Gravity.TOP,
                paint
        );

        paint.drop();
    }
}
