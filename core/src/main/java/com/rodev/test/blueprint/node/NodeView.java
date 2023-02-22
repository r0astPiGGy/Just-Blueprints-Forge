package com.rodev.test.blueprint.node;

import com.rodev.test.Colors;
import com.rodev.test.blueprint.ChildRoot;
import com.rodev.test.blueprint.data.variable.VariableTypeRegistry;
import com.rodev.test.blueprint.pin.Pin;
import com.rodev.test.blueprint.pin.PinRowView;
import com.rodev.test.blueprint.pin.Position;
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

public class NodeView extends LinearLayout implements BPNode {

    private final LinearLayout inputRowsContainer = createRowContainer(Gravity.START);
    private final LinearLayout outputRowsContainer = createRowContainer(Gravity.END);

    private Boolean selected = false;

    private final List<PinRowView> outputPins = new LinkedList<>();
    private final List<PinRowView> inputPins = new LinkedList<>();

    private final NodeTouchHandler<NodeView> nodeTouchHandler;

    public NodeView(int headerColor, String nodeName) {
        nodeTouchHandler = new NodeTouchHandler<>(this, this::updatePinsPosition);

        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        var nodeLabel = new TextView();
        nodeLabel.setText(nodeName);
        nodeLabel.setPadding(0, 0, dp(30), dp(10));
        addView(nodeLabel);

        setBackground(new NodeDrawable(nodeLabel::getHeight, headerColor));

        RelativeLayout allRowsContainer = new RelativeLayout();
        {
            inputRowsContainer.setId(4155423);
            var params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_PARENT_START);
            allRowsContainer.addView(inputRowsContainer, params);
        }
        {
            outputRowsContainer.setId(541455);
            var params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.addRule(RelativeLayout.ALIGN_END, inputRowsContainer.getId());
            params.addRule(RelativeLayout.ALIGN_PARENT_END);

            allRowsContainer.addView(outputRowsContainer, params);
        }

        addView(allRowsContainer);
    }

    private static LinearLayout createRowContainer(int gravity) {
        var linearLayout = new LinearLayout();
        linearLayout.setOrientation(VERTICAL);
        linearLayout.setMinimumWidth(dp(20));
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        linearLayout.setGravity(gravity);

        return linearLayout;
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
    public @Nullable BPNode getPrevious() {
        return null;
    }

    @Override
    public @Nullable BPNode getNext() {
        return null;
    }
}
