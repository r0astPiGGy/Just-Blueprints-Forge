package com.rodev.jbpcore.ui.node.getter;

import com.rodev.jbpcore.data.selectors.SelectorGroup;
import com.rodev.jbpcore.ui.node.NodeView;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.ui.pin.PinRowView;
import com.rodev.jbpcore.ui.pin.default_input_value.DefaultSelectorInputView;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.drawable.ImageDrawable;

public class SelectorVariableGetterNode extends NodeView {

    private PinRowView output;
    private final SelectorGroup selectorGroup;

    public SelectorVariableGetterNode(Context context, int headerColor, String id, String name, ImageDrawable icon, SelectorGroup selectorGroup) {
        super(context, headerColor, id, name, icon);

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

    private class SelectorValueView extends DefaultSelectorInputView {

        public SelectorValueView(SelectorGroup selectorGroup) {
            super(SelectorVariableGetterNode.this.getContext(), selectorGroup);
        }

        @Override
        public void show() {}

        @Override
        public void hide() {}
    }
}
