package com.rodev.jbpcore.ui.pin.default_input_value;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.data.selectors.Selector;
import com.rodev.jbpcore.data.selectors.SelectorGroup;
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

public class DefaultSelectorInputView extends Spinner implements DefaultInputValue {

    private final List<Selector> values;

    public DefaultSelectorInputView(Context context, SelectorGroup selectorGroup) {
        super(context);
        values = selectorGroup.selectors();
        ArrayAdapter<Selector> arrayAdapter = new CustomArrayAdapter<>(context, values);

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
        if(getSelectedItem() instanceof Selector value) {
            return value.id();
        }

        return null;
    }

    @Override
    public void setDefaultValue(String value) {
        int i = 0;
        for(var a : values) {
            if(a.id().equalsIgnoreCase(value)) {
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
