package com.rodev.test.contextmenu;

import com.rodev.test.Colors;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.font.FontPaint;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.TextView;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TreeNodeHeaderLabel extends LinearLayout {

    private boolean arrowEnabled = true;

    private final Consumer<Integer> onPaddingChangedCallback;

    public TreeNodeHeaderLabel(String text, Consumer<Integer> onPaddingChangedCallback) {
        this.onPaddingChangedCallback = onPaddingChangedCallback;

        setOrientation(HORIZONTAL);

        var headerLabel = new TextView();
        headerLabel.setText(text);
        headerLabel.setTextStyle(FontPaint.BOLD);
        headerLabel.setTextSize(View.sp(13));

        addView(headerLabel, new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setBackground(new Background());
    }

    public boolean isArrowEnabled() {
        return arrowEnabled;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int padding = getMeasuredHeight();

        setPadding(padding, 0, 0, 0);
        onPaddingChangedCallback.accept(padding);
    }

    public void setArrowEnabled(boolean enabled) {
        this.arrowEnabled = enabled;
    }

    private class Background extends Drawable {

        @Override
        public void draw(@NotNull Canvas canvas) {
            final Rect r = getBounds();
            final int width = r.height();

            float cy = r.exactCenterY();
            Paint paint = Paint.get();
            paint.setColor(Colors.WHITE);
            paint.setStrokeWidth(1.2f);

            // Controls size of the triangle
            int offset = 3;

            int left = r.left + offset;
            int top = r.top + offset;
            int right = r.left + width - offset;
            int bottom = r.bottom - offset;
            float centerY = r.top + cy;

            if(isArrowEnabled()) {
                canvas.drawTriangle(right, top, right, bottom, left, bottom, paint);
            } else {
                right -= offset;
                canvas.drawRoundLine(left, top, right, centerY, paint);
                canvas.drawRoundLine(right, centerY, left, bottom, paint);
                canvas.drawRoundLine(left, bottom, left, top, paint);
            }
        }
    }

}
