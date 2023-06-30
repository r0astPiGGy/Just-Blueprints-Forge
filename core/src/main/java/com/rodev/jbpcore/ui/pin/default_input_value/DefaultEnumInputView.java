package com.rodev.jbpcore.ui.pin.default_input_value;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.data.action.pin_type.EnumPinType;
import com.rodev.jbpcore.data.action.EnumValue;
import com.rodev.jbpcore.data.variable.DefaultInputValue;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.ArrayAdapter;
import icyllis.modernui.widget.Spinner;

import javax.annotation.Nonnull;
import java.util.List;

public class DefaultEnumInputView extends Spinner implements DefaultInputValue {

    private final List<EnumValue> enumValueList;

    public DefaultEnumInputView(Context context, EnumPinType pinType) {
        super(context);
        enumValueList = pinType.values();
        ArrayAdapter<EnumValue> arrayAdapter = new CustomArrayAdapter<>(context, enumValueList);

        setAdapter(arrayAdapter);

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
                int r = (int) Math.ceil(mRadius / 2.5f);
                padding.set(r, r, r, r);
                return true;
            }
        });
    }

    @Override
    public String getDefaultValue() {
        if(getSelectedItem() instanceof EnumValue value) {
            return "\"" + value.key() + "\"";
        }

        return null;
    }

    @Override
    public void setDefaultValue(String value) {
        value = value.replace("\"", "");
        int i = 0;
        for(var a : enumValueList) {
            if(a.key().equalsIgnoreCase(value)) {
                setSelection(i);
            }
            i++;
        }
    }

    @Override
    public View asView() {
        return this;
    }
}
