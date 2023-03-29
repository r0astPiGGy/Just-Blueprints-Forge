package com.rodev.jbpcore.blueprint.node.impl.getter;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.blueprint.data.selectors.Selector;
import com.rodev.jbpcore.blueprint.data.selectors.SelectorGroup;
import com.rodev.jbpcore.blueprint.data.variable.DefaultInputValue;
import com.rodev.jbpcore.blueprint.node.impl.NodeView;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.PinRowView;
import com.rodev.jbpcore.blueprint.pin.default_input_value.CustomArrayAdapter;
import com.rodev.jbpcore.blueprint.pin.default_input_value.DefaultSelectorInputView;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.ArrayAdapter;
import icyllis.modernui.widget.Spinner;

import javax.annotation.Nonnull;
import java.util.List;

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

    private static class SelectorValueView extends DefaultSelectorInputView {

        public SelectorValueView(SelectorGroup selectorGroup) {
            super(selectorGroup);
        }

        @Override
        public void show() {}

        @Override
        public void hide() {}
    }
}
