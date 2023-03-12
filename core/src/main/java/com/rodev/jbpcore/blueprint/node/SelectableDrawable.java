package com.rodev.jbpcore.blueprint.node;

import com.rodev.jbpcore.Colors;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;

import javax.annotation.Nonnull;

import static icyllis.modernui.view.View.dp;

public class SelectableDrawable extends Drawable {

    protected boolean isSelected;
    protected final int mRadius;
    protected final int selectionOffset;
    protected int backgroundColor = Colors.NODE_BACKGROUND;
    protected int padding = 0;

    public SelectableDrawable() {
        this(3, dp(8));
    }

    public SelectableDrawable(int selectionOffset, int mRadius) {
        this.selectionOffset = selectionOffset;
        this.mRadius = mRadius;
    }

    public void setSelected(boolean value) {
        isSelected = value;
    }

    @Override
    public void draw(@Nonnull Canvas canvas) {
        Paint paint = Paint.take();
        Rect b = getBounds();

        drawSelectionOutlineIfSelected(canvas, paint);

        onBackgroundColorSet(paint);
        canvas.drawRoundRect(
                b.left + selectionOffset,
                b.top + selectionOffset,
                b.right - selectionOffset,
                b.bottom - selectionOffset,
                mRadius,
                paint
        );

        paint.drop();
    }

    protected void drawSelectionOutlineIfSelected(Canvas canvas, Paint paint) {
        var b = getBounds();

        if (isSelected) {
            paint.setColor(Colors.YELLOW);
            canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, mRadius, paint);
        }
    }

    protected void onBackgroundColorSet(Paint paint) {
        paint.setColor(backgroundColor);
    }

    @Override
    public boolean getPadding(@Nonnull Rect padding) {
        var r = this.padding = calcPadding();
        padding.set(r, r, r, r);

        return true;
    }

    protected int calcPadding() {
        return (int) Math.ceil(mRadius / 2f) + selectionOffset;
    }


}
