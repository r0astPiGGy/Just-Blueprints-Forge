package com.rodev.test.blueprint.node;

import com.rodev.test.Colors;
import com.rodev.test.Fonts;
import com.rodev.test.blueprint.ChildRoot;
import com.rodev.test.blueprint.data.variable.VariableTypeRegistry;
import com.rodev.test.blueprint.pin.Pin;
import com.rodev.test.blueprint.pin.PinRowView;
import com.rodev.test.blueprint.pin.Position;
import com.rodev.test.utils.TextViewCreationListener;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.text.Typeface;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class NodeView extends LinearLayout implements BPNode {

    private final LinearLayout inputRowsContainer;
    private final LinearLayout outputRowsContainer;

    private Boolean selected = false;

    private final List<PinRowView> outputPins = new LinkedList<>();
    private final List<PinRowView> inputPins = new LinkedList<>();

    private final String id;
    private final String nodeName;

    private final NodeTouchHandler<NodeView> nodeTouchHandler;

    public NodeView(int headerColor, String id, String name) {
        this.id = id;
        this.nodeName = name;

        nodeTouchHandler = new NodeTouchHandler<>(this, this::updatePinsPosition);

        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        var nodeHeader = createNodeHeader();
        addView(nodeHeader);

        setBackground(new NodeDrawable(nodeHeader::getHeight, headerColor));

        LinearLayout allRowsContainer = new LinearLayout();
        allRowsContainer.setOrientation(HORIZONTAL);
        allRowsContainer.setLayoutParams(new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
        ));

        var spaceHelperView = new SpaceHelperView();
        inputRowsContainer = createRowContainer(Gravity.START);
        outputRowsContainer = createRowContainer(new LinearLayout(){
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

        addView(allRowsContainer);
    }

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
        var drawable = new ImageDrawable("actions", id + ".png");
        icon.setBackground(drawable);
        icon.setLayoutParams(new ViewGroup.LayoutParams(dp(30), dp(30)));

        return icon;
    }

    private TextView createLabel() {
        var nodeLabel = new TextView();
        nodeLabel.setText(nodeName);
        nodeLabel.setGravity(Gravity.CENTER);

        TextViewCreationListener.onNodeLabelCreated(nodeLabel);
        nodeLabel.setPadding(dp(6), 0, 0, 0);

        return nodeLabel;
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

    @Override
    public int[] getChildCoordinates(View view) {
        return ChildRoot.getChildCoordinates(this, view);
    }

    @Override
    public boolean onTouchEvent(@NotNull MotionEvent event) {
        return nodeTouchHandler.handle(event);
    }

    private void updatePinsPosition() {
        outputPins.forEach(pinRowView -> pinRowView.getPinView().getPin().onPositionUpdate());
        inputPins.forEach(pinRowView -> pinRowView.getPinView().getPin().onPositionUpdate());
    }

    public void addOutput(PinRowView pinRowView) {
        outputRowsContainer.addView(pinRowView);
        outputPins.add(pinRowView);
    }

    public void addInput(PinRowView pinRowView) {
        inputRowsContainer.addView(pinRowView);
        inputPins.add(pinRowView);
    }

    @Override
    public void select() {
        selected = true;
        invalidateBackground();
    }

    @Override
    public void deselect() {
        selected = false;
        invalidateBackground();
    }

    private void invalidateBackground() {
        if(getBackground() instanceof SelectableDrawable background) {
            background.setSelected(isNodeSelected());
            invalidateDrawable(background);
        }
    }

    @Override
    public boolean isNodeSelected() {
        return selected;
    }

    @Override
    public void onDelete() {

    }

    @Override
    public void setNodeTouchListener(NodeTouchListener listener) {
        nodeTouchHandler.setNodeTouchListener(listener);
    }

    @Override
    public void setNodeMoveListener(NodeMoveListener listener) {
        nodeTouchHandler.setNodeMoveListener(listener);
    }

    @Override
    public void addInputPin(Pin pin, String name) {
        var rowView = pin.createRowView();
        rowView.setText(name);

        VariableTypeRegistry.onPinRowViewCreated(pin, rowView);

        addInput(rowView);
    }

    @Override
    public void addOutputPin(Pin pin, String name) {
        addOutput(pin.createRowView().setText(name));
    }

    @Override
    public void forEachPin(Consumer<Pin> pinConsumer) {
        outputPins.forEach(row -> pinConsumer.accept(row.getPinView().getPin()));
        inputPins.forEach(row -> pinConsumer.accept(row.getPinView().getPin()));
    }

    @Override
    public @Nullable BPNode getPrevious() {
        return null;
    }

    @Override
    public @Nullable BPNode getNext() {
        return null;
    }
}
