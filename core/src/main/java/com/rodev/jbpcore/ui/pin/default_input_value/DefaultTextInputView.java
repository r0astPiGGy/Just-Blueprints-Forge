package com.rodev.jbpcore.ui.pin.default_input_value;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.data.variable.DefaultInputValue;
import com.rodev.jbpcore.handlers.TextViewCreationListener;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.EditText;

import javax.annotation.Nonnull;

public class DefaultTextInputView extends EditText implements DefaultInputValue {

    public DefaultTextInputView(Context context) {
        super(context);
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
                Paint paint = Paint.obtain();
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
        return "\"" + getText() + "\"";
    }

    @Override
    public void setDefaultValue(String value) {
        if(value.isEmpty()) return;
        var builder = new StringBuilder(value);
        builder.deleteCharAt(0).deleteCharAt(builder.length() - 1);
        setText(builder.toString());
    }

    @Override
    public View asView() {
        return this;
    }
}
