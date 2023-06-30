package com.rodev.jbpcore.ui.contextmenu.tree;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.handlers.TextViewCreationListener;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.TextView;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TreeNodeHeaderLabel extends LinearLayout {

    private final TextView headerLabel;
    private boolean arrowEnabled = true;

    private final Consumer<Integer> onPaddingChangedCallback;

    public TreeNodeHeaderLabel(Context context, String text, Consumer<Integer> onPaddingChangedCallback) {
        super(context);
        this.onPaddingChangedCallback = onPaddingChangedCallback;

        headerLabel = new TextView(context);

        setOrientation(HORIZONTAL);

        headerLabel.setText(text);
        TextViewCreationListener.onContextMenuCategoryTextCreated(headerLabel);

        addView(headerLabel, new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        setBackground(new Background());
    }

    public void setText(String text) {
        headerLabel.setText(text);
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
            Paint paint = Paint.obtain();
            paint.setColor(Colors.WHITE);
            paint.setStrokeWidth(1.2f);

            // Controls size of the triangle
            int offset = 3;

            int left = r.left + offset;
            int top = r.top + offset;
            int right = r.left + width - offset;
            int bottom = r.bottom - offset;
            float centerY = r.top + cy;

            // TODO textures
//            if(isArrowEnabled()) {
//                canvas.drawTriangle(right, top, right, bottom, left, bottom, paint);
//            } else {
//                right -= offset;
//                canvas.drawRoundLine(left, top, right, centerY, paint);
//                canvas.drawRoundLine(right, centerY, left, bottom, paint);
//                canvas.drawRoundLine(left, bottom, left, top, paint);
//            }
        }
    }

}
