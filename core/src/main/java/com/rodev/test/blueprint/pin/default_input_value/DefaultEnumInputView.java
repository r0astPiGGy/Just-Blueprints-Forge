package com.rodev.test.blueprint.pin.default_input_value;

import com.rodev.test.Colors;
import com.rodev.test.blueprint.data.action.EnumPinType;
import com.rodev.test.blueprint.data.action.EnumValue;
import com.rodev.test.blueprint.data.action.PinType;
import com.rodev.test.blueprint.data.variable.DefaultInputValue;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.ArrayAdapter;
import icyllis.modernui.widget.Spinner;
import icyllis.modernui.widget.SpinnerAdapter;

import javax.annotation.Nonnull;

public class DefaultEnumInputView extends Spinner implements DefaultInputValue {

    public DefaultEnumInputView(EnumPinType pinType) {
        ArrayAdapter<EnumValue> arrayAdapter = new ArrayAdapter<>(pinType.values());

        setAdapter(arrayAdapter);

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
                int r = (int) Math.ceil(mRadius / 2.5f);
                padding.set(r, r, r, r);
                return true;
            }
        });
    }

    @Override
    public String getDefaultValue() {
        if(getSelectedItem() instanceof EnumValue value) {
            return value.key();
        }

        return null;
    }

    @Override
    public View asView() {
        return this;
    }
}
