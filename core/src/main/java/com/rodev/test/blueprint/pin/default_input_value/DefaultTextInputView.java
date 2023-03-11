package com.rodev.test.blueprint.pin.default_input_value;

import com.rodev.test.Colors;
import com.rodev.test.blueprint.data.variable.DefaultInputValue;
import com.rodev.test.utils.TextViewCreationListener;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.EditText;

import javax.annotation.Nonnull;

public class DefaultTextInputView extends EditText implements DefaultInputValue {

    public DefaultTextInputView() {
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        TextViewCreationListener.onDefaultTextInputViewCreated(this);
        setHint("Текст");
        //setTextColor(Colors.NODE_BACKGROUND);
        setBackground(new Drawable() {
            private final int mRadius = dp(4);

            @Override
            public void draw(@Nonnull Canvas canvas) {
                Paint paint = Paint.get();
                paint.setColor(Colors.WHITE);
                paint.setStyle(Paint.STROKE);
                paint.setStrokeWidth(1.2f);
                Rect b = getBounds();
                canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, mRadius, paint);
            }

            @Override
            public boolean getPadding(@Nonnull Rect padding) {
                int r = (int) Math.ceil(mRadius / 1.5f);
                padding.set(r, r, r, r);
                return true;
            }
        });
        setTextSize(sp(14));
    }

    @Override
    public String getDefaultValue() {
        return getText().toString();
    }

    @Override
    public void setDefaultValue(String value) {
        setText(value);
    }

    @Override
    public View asView() {
        return this;
    }
}
