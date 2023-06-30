package com.rodev.jbpcore.blueprint.node;

import com.rodev.jbpcore.Colors;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;

import javax.annotation.Nonnull;

public class SelectableDrawable extends Drawable {

    protected boolean isSelected;
    public int mRadius;
    protected final int selectionOffset;
    protected int backgroundColor = Colors.NODE_BACKGROUND;
    protected int padding = 0;

    public SelectableDrawable() {
        this(3);
    }

    public SelectableDrawable(int selectionOffset) {
        this.selectionOffset = selectionOffset;
    }

    public void setSelected(boolean value) {
        isSelected = value;
    }

    @Override
    public void draw(@Nonnull Canvas canvas) {
        Paint paint = Paint.obtain();
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

        paint.recycle();
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
