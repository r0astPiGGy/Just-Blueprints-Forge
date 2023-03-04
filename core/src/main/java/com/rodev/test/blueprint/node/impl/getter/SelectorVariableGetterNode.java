package com.rodev.test.blueprint.node.impl.getter;

import com.rodev.test.Colors;
import com.rodev.test.blueprint.data.action.EnumValue;
import com.rodev.test.blueprint.data.selectors.Selector;
import com.rodev.test.blueprint.data.selectors.SelectorGroup;
import com.rodev.test.blueprint.data.variable.DefaultInputValue;
import com.rodev.test.blueprint.node.impl.NodeView;
import com.rodev.test.blueprint.pin.Pin;
import com.rodev.test.blueprint.pin.PinRowView;
import com.rodev.test.blueprint.pin.default_input_value.CustomArrayAdapter;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.ArrayAdapter;
import icyllis.modernui.widget.Spinner;

import javax.annotation.Nonnull;

public class SelectorVariableGetterNode extends NodeView {

    private PinRowView output;
    private final SelectorGroup selectorGroup;

    public SelectorVariableGetterNode(int headerColor, String id, String name, ImageDrawable icon, SelectorGroup selectorGroup) {
        super(headerColor, id, name, icon);

        this.selectorGroup = selectorGroup;
    }

    @Override
    public void addInput(PinRowView pinRowView) {
        throw new IllegalStateException("Getter node cannot have input pins");
    }

    @Override
    public void addInputPin(Pin pin, String name) {
        throw new IllegalStateException("Getter node cannot have input pins");
    }

    @Override
    public void addOutput(PinRowView pinRowView) {
        if(output != null) {
            throw new IllegalStateException("Getter node cannot have more than one output");
        }

        output = pinRowView;

        super.addOutput(pinRowView);

        if(selectorGroup != null) {
            pinRowView.setText("Селектор");
            pinRowView.addDefaultValueView(new SelectorValueView(selectorGroup));
        }
    }

    private static class SelectorValueView extends Spinner implements DefaultInputValue {

        public SelectorValueView(SelectorGroup selectorGroup) {
            ArrayAdapter<Selector> arrayAdapter = new CustomArrayAdapter<>(selectorGroup.selectors());

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

        @Override
        public void hide() {}

        @Override
        public void show() {}
    }
}
