package com.rodev.jbpcore.ui.node;

import com.rodev.jbpcore.blueprint.node.BaseNode;
import com.rodev.jbpcore.ui.drawable.NodeDrawable;
import com.rodev.jbpcore.ui.pin.PinRowView;
import com.rodev.jbpcore.handlers.TextViewCreationListener;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.TextView;

public class NodeView extends BaseNode {

    private final LinearLayout inputRowsContainer;
    private final LinearLayout outputRowsContainer;

    private final String title;
    private TextView subtitleView;

    private final LinearLayout nodeHeader;

    private final ImageDrawable iconDrawable;

    public NodeView(Context context, int headerColor, String id, String title, ImageDrawable iconDrawable) {
        super(context, id);
        this.title = title;
        this.iconDrawable = iconDrawable;

        nodeHeader = createNodeHeader();
        addView(nodeHeader);

        LinearLayout allRowsContainer = new LinearLayout(getContext());
        allRowsContainer.setOrientation(HORIZONTAL);
        allRowsContainer.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        ));

        var spaceHelperView = new SpaceHelperView(context);
        inputRowsContainer = createRowContainer(context, Gravity.START);
        outputRowsContainer = createRowContainer(new LinearLayout(getContext()) {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
                spaceHelperView.onLastMeasure(inputRowsContainer.getMeasuredWidth(), getMeasuredWidth(), nodeHeader.getMeasuredWidth());
            }
        }, Gravity.END);
        inputRowsContainer.setPadding(dp(3), 0, dp(15), dp(5));
        outputRowsContainer.setPadding(dp(15), 0, dp(3), dp(5));
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
        var nodeHeader = new LinearLayout(getContext());
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
        var icon = new View(getContext());
        icon.setBackground(this.iconDrawable);

        var params = new LayoutParams(dp(30), dp(30));
        params.gravity = Gravity.TOP | Gravity.LEFT;
        icon.setLayoutParams(params);

        return icon;
    }

    private LinearLayout createLabel() {
        var nodeHeader = new LinearLayout(getContext());
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
        var title = new TextView(getContext());
        title.setText(this.title);

        TextViewCreationListener.onNodeTitleCreated(title);

        return title;
    }

    private TextView createSubtitle() {
        var subtitleView = new TextView(getContext());

        TextViewCreationListener.onNodeSubtitleCreated(subtitleView);
        subtitleView.setVisibility(GONE);

        return subtitleView;
    }

    private static LinearLayout createRowContainer(LinearLayout linearLayout, int gravity) {
        linearLayout.setOrientation(VERTICAL);
        linearLayout.setMinimumWidth(linearLayout.dp(20));
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        linearLayout.setGravity(gravity);

        return linearLayout;
    }

    private static LinearLayout createRowContainer(Context context, int gravity) {
        return createRowContainer(new LinearLayout(context), gravity);
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
