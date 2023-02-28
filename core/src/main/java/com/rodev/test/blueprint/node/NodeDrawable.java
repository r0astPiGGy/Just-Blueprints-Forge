package com.rodev.test.blueprint.node;

import com.rodev.test.Colors;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Color;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.math.Rect;
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

    public NodeDrawable(Supplier<Integer> headerHeightSupplier, int headerColor, int selectionOffset, int mRadius) {
        super(selectionOffset, mRadius);
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

        // TODO Use sides чтобы убрать костыль
        canvas.drawRoundRect(
                b.left + selectionOffset,
                b.top + selectionOffset,
                b.right - selectionOffset,
                b.top + headerHeight,
                mRadius,
                paint
        );

        canvas.drawRect(
                b.left + selectionOffset,
                b.top + selectionOffset + mRadius,
                b.right - selectionOffset,
                b.top + headerHeight,
                paint
        );

        paint.drop();
    }
}
