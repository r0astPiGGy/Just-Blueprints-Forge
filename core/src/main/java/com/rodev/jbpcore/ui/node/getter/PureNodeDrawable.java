package com.rodev.jbpcore.ui.node.getter;

import com.rodev.jbpcore.blueprint.node.SelectableDrawable;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import org.jetbrains.annotations.NotNull;

import static icyllis.modernui.view.View.dp;

public class PureNodeDrawable extends SelectableDrawable {

    private final int[] headerColors;

    public PureNodeDrawable(int headerColor) {
        super(3, dp(15));
        headerColors = new int[]{headerColor, backgroundColor, backgroundColor, headerColor};
    }

    @Override
    public void draw(@NotNull Canvas canvas) {
        super.draw(canvas);

        var b = getBounds();
        var paint = Paint.take();

        paint.setColors(headerColors);

        var tempPadding = ((float) padding) / 1.5f;

        canvas.drawRoundRect(
                b.left + tempPadding,
                b.top + tempPadding,
                b.right - tempPadding,
                b.bottom - tempPadding,
                ((float) (mRadius) / 2) * 1.5f,
                paint
        );

        paint.drop();
    }
}
