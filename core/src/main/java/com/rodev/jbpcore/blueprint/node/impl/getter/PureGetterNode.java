package com.rodev.jbpcore.blueprint.node.impl.getter;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.blueprint.data.selectors.Selector;
import com.rodev.jbpcore.blueprint.data.selectors.SelectorGroup;
import com.rodev.jbpcore.blueprint.data.variable.DefaultInputValue;
import com.rodev.jbpcore.blueprint.node.BaseNode;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.PinRowView;
import com.rodev.jbpcore.blueprint.pin.default_input_value.CustomArrayAdapter;
import com.rodev.jbpcore.utils.TextViewCreationListener;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.ArrayAdapter;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.Spinner;
import icyllis.modernui.widget.TextView;

import javax.annotation.Nonnull;
import java.util.List;

public class PureGetterNode extends BaseNode {

    private PinRowView output;
    private final SelectorGroup selectorGroup;

    private final ImageDrawable iconDrawable;

    private TextView subtitleView;

    private final String title;

    public PureGetterNode(int headerColor, String id, String name, ImageDrawable icon, SelectorGroup group) {
        super(id);

        this.title = name;

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        this.iconDrawable = icon;

        this.selectorGroup = group;

        var nodeHeader = createNodeHeader();

        addView(nodeHeader);

        setBackground(new PureNodeDrawable(headerColor));
    }

    private LinearLayout createNodeHeader() {
        var nodeHeader = new LinearLayout();
        nodeHeader.setOrientation(HORIZONTAL);
        var params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, dp(3), 0, dp(3));
        nodeHeader.setPadding(0, 0, dp(30), 0);
        nodeHeader.setLayoutParams(params);
        nodeHeader.setGravity(Gravity.CENTER | Gravity.START);

        var icon = createIcon();
        var label = createLabel();

        nodeHeader.addView(icon);
        nodeHeader.addView(label);

        return nodeHeader;
    }

    private View createIcon() {
        var icon = new View();
        icon.setBackground(this.iconDrawable);

        var params = new LayoutParams(dp(30), dp(30));
        params.gravity = Gravity.TOP | Gravity.LEFT;
        icon.setLayoutParams(params);

        return icon;
    }

    private LinearLayout createLabel() {
        var nodeHeader = new LinearLayout();
        nodeHeader.setOrientation(VERTICAL);
        var params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nodeHeader.setPadding(dp(6), 0, 0, 0);
        nodeHeader.setGravity(Gravity.CENTER | Gravity.START);
        nodeHeader.setLayoutParams(params);

        var title = createTitle();
        var subtitle = subtitleView = createSubtitle();

        nodeHeader.addView(title);
        nodeHeader.addView(subtitle);

        return nodeHeader;
    }

    private TextView createTitle() {
        var title = new TextView();
        title.setText(this.title);

        TextViewCreationListener.onNodeTitleCreated(title);

        return title;
    }

    private TextView createSubtitle() {
        var subtitleView = new TextView();

        TextViewCreationListener.onNodeSubtitleCreated(subtitleView);
        subtitleView.setVisibility(GONE);

        return subtitleView;
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
    public void setSubtitle(String subtitle) {
        if(subtitle == null || subtitle.isEmpty()) {
            subtitleView.setVisibility(GONE);
        } else {
            subtitleView.setText(subtitle);
            subtitleView.setVisibility(VISIBLE);
        }
    }

    @Override
    public void addOutput(PinRowView pinRowView) {
        if(output != null) {
            throw new IllegalStateException("Getter node cannot have more than one output");
        }

        output = pinRowView;

        super.addOutput(pinRowView);
        addView(pinRowView);

        if(selectorGroup != null) {
            pinRowView.setText("Селектор");
            pinRowView.addDefaultValueView(new SelectorValueView(selectorGroup));
        }
    }

    private static class SelectorValueView extends Spinner implements DefaultInputValue {

        private final List<Selector> values;

        public SelectorValueView(SelectorGroup selectorGroup) {
            values = selectorGroup.selectors();
            ArrayAdapter<Selector> arrayAdapter = new CustomArrayAdapter<>(values);

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

        @Override
        public void hide() {}

        @Override
        public void show() {}
    }
}
