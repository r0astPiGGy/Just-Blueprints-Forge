package com.rodev.test.blueprint.node.impl;

import com.rodev.test.blueprint.node.BaseNode;
import com.rodev.test.blueprint.pin.PinRowView;
import com.rodev.test.utils.TextViewCreationListener;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.TextView;

import java.util.HashMap;

public class NodeView extends BaseNode {

    private final LinearLayout inputRowsContainer;
    private final LinearLayout outputRowsContainer;

    private final String title;
    private TextView subtitleView;

    private final LinearLayout nodeHeader;

    private final ImageDrawable iconDrawable;

    public NodeView(int headerColor, String id, String title, ImageDrawable iconDrawable) {
        super(id);
        this.title = title;
        this.iconDrawable = iconDrawable;

        nodeHeader = createNodeHeader();
        addView(nodeHeader);

        LinearLayout allRowsContainer = new LinearLayout();
        allRowsContainer.setOrientation(HORIZONTAL);
        allRowsContainer.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        ));

        var spaceHelperView = new SpaceHelperView();
        inputRowsContainer = createRowContainer(Gravity.START);
        outputRowsContainer = createRowContainer(new LinearLayout() {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                spaceHelperView.onLastMeasure(inputRowsContainer.getMeasuredWidth(), getMeasuredWidth(), nodeHeader.getMeasuredWidth());
            }
        }, Gravity.END);
        outputRowsContainer.setPadding(dp(10), 0, 0, 0);
        {
            inputRowsContainer.setId(4155423);
            var params = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            allRowsContainer.addView(inputRowsContainer, params);
        }
        allRowsContainer.addView(spaceHelperView);
        {
            outputRowsContainer.setId(541455);
            var params = new LinearLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            allRowsContainer.addView(outputRowsContainer, params);
        }


        setBackground(new NodeDrawable(nodeHeader::getHeight, headerColor));
        addView(allRowsContainer);
    }

    @Override
    protected void onBackgroundInit() {}

    private LinearLayout createNodeHeader() {
        var nodeHeader = new LinearLayout();
        nodeHeader.setOrientation(HORIZONTAL);
        var params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, dp(10));
        nodeHeader.setPadding(0, 0, dp(30), dp(2));
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
        var params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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

    private static LinearLayout createRowContainer(LinearLayout linearLayout, int gravity) {
        linearLayout.setOrientation(VERTICAL);
        linearLayout.setMinimumWidth(dp(20));
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        linearLayout.setGravity(gravity);

        return linearLayout;
    }

    private static LinearLayout createRowContainer(int gravity) {
        return createRowContainer(new LinearLayout(), gravity);
    }

    public void addOutput(PinRowView pinRowView) {
        super.addOutput(pinRowView);
        outputRowsContainer.addView(pinRowView);
    }

    public void addInput(PinRowView pinRowView) {
        super.addInput(pinRowView);
        inputRowsContainer.addView(pinRowView);
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
}
