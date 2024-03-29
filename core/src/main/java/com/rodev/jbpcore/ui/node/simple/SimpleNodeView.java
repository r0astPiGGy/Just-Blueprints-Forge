package com.rodev.jbpcore.ui.node.simple;

import com.rodev.jbpcore.blueprint.node.BaseNode;
import com.rodev.jbpcore.blueprint.node.SelectableDrawable;
import com.rodev.jbpcore.ui.pin.PinRowView;
import com.rodev.jbpcore.utils.ParamsBuilder;
import com.rodev.jbpcore.handlers.TextViewCreationListener;
import icyllis.modernui.core.Context;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.TextView;

public class SimpleNodeView extends BaseNode {

    private final LinearLayout inputRowsContainer;
    private final LinearLayout outputRowsContainer;

    public SimpleNodeView(Context context, String id, String displayName) {
        super(context, id);

        inputRowsContainer = new LinearLayout(context);
        outputRowsContainer = new LinearLayout(context);

        var rootLayout = new RelativeLayout(context);

        //region textView
        var textView = new TextView(context);
        textView.setText(displayName);
        TextViewCreationListener.setDefaultFont(textView);

        ParamsBuilder.using(RelativeLayout.LayoutParams::new)
                .wrapContent()
                .setup(p -> {
                    p.addRule(RelativeLayout.CENTER_VERTICAL);
                    p.addRule(RelativeLayout.CENTER_HORIZONTAL);
                })
                .applyTo(textView);

        rootLayout.addView(textView);
        //endregion

        ParamsBuilder.using(LinearLayout.LayoutParams::new)
                .wrapContent()
                .applyTo(rootLayout);

        addView(rootLayout);

        // todo: create SimpleNodeDrawable (with icon)
        setBackground(new SelectableDrawable());
    }

    @Override
    public void setSubtitle(String subtitle) {}

    @Override
    protected void addInput(PinRowView pinRowView) {
        super.addInput(pinRowView);

        inputRowsContainer.addView(pinRowView);
    }

    @Override
    protected void addOutput(PinRowView pinRowView) {
        super.addOutput(pinRowView);

        outputRowsContainer.addView(pinRowView);
    }
}
