package com.rodev.test.blueprint.node.impl.getter;

import com.rodev.test.Colors;
import com.rodev.test.blueprint.data.action.EnumValue;
import com.rodev.test.blueprint.data.selectors.Selector;
import com.rodev.test.blueprint.data.selectors.SelectorGroup;
import com.rodev.test.blueprint.data.variable.DefaultInputValue;
import com.rodev.test.blueprint.node.BaseNode;
import com.rodev.test.blueprint.pin.Pin;
import com.rodev.test.blueprint.pin.PinRowView;
import com.rodev.test.blueprint.pin.default_input_value.CustomArrayAdapter;
import com.rodev.test.utils.TextViewCreationListener;
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

public class PropertyGetterNode extends BaseNode {

    private PinRowView input;
    private PinRowView output;

    private final ImageDrawable iconDrawable;

    private TextView subtitleView;

    private final String title;

    private final Runnable headerCallback;

    public PropertyGetterNode(int headerColor, String id, String name, ImageDrawable icon) {
        super(id);

        this.title = name;

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        this.iconDrawable = icon;

        headerCallback = () -> {
            var nodeHeader = createNodeHeader();

            addView(nodeHeader);
        };

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
    public void setSubTitle(String subTitle) {
        if(subTitle == null || subTitle.isEmpty()) {
            subtitleView.setVisibility(GONE);
        } else {
            subtitleView.setText(subTitle);
            subtitleView.setVisibility(VISIBLE);
        }
    }

    @Override
    public void addInput(PinRowView pinRowView) {
        if(input != null) {
            throw new IllegalStateException("Property getter node cannot have more than one output");
        }

        input = pinRowView;

        super.addOutput(pinRowView);
        addView(pinRowView);

        pinRowView.setText("");
        pinRowView.disableMinimumWidth();

        headerCallback.run();
    }

    @Override
    public void addOutput(PinRowView pinRowView) {
        if(output != null) {
            throw new IllegalStateException("Property getter node cannot have more than one output");
        }

        output = pinRowView;

        super.addOutput(pinRowView);
        addView(pinRowView);

        pinRowView.setText("");
        pinRowView.disableMinimumWidth();
    }
}
