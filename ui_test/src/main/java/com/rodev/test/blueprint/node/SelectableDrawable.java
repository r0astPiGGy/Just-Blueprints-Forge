package com.rodev.test.blueprint.node;

import com.rodev.test.Colors;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Color;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;

import javax.annotation.Nonnull;

import static icyllis.modernui.view.View.dp;

public class SelectableDrawable extends Drawable {

    private boolean isSelected;
    private final int mRadius;
    private final int selectionOffset;

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
        Paint paint = Paint.get();
        Rect b = getBounds();

        if (isSelected) {
            paint.setColor(Colors.YELLOW);
            canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, mRadius, paint);
        }

        paint.setColor(Color.rgb(10, 10, 10));
        canvas.drawRoundRect(
                b.left + selectionOffset,
                b.top + selectionOffset,
                b.right - selectionOffset,
                b.bottom - selectionOffset,
                mRadius,
                paint
        );
    }

    @Override
    public boolean getPadding(@Nonnull Rect padding) {
        int r = (int) Math.ceil(mRadius / 2f) + (selectionOffset * 2);
        padding.set(r, r, r, r);
        return true;
    }


}
