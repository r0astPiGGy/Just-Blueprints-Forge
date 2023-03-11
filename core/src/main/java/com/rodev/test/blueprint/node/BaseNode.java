package com.rodev.test.blueprint.node;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.rodev.test.blueprint.ChildRoot;
import com.rodev.test.blueprint.data.variable.VariableTypeRegistry;
import com.rodev.test.blueprint.pin.Pin;
import com.rodev.test.blueprint.pin.PinRowView;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public abstract class BaseNode extends LinearLayout implements BPNode {

    private Boolean selected = false;

    private final List<PinRowView> outputPins = new LinkedList<>();
    private final List<PinRowView> inputPins = new LinkedList<>();

    private final String id;

    private final NodeTouchHandler<BaseNode> nodeTouchHandler;

    public BaseNode(String id) {
        this.id = id;

        nodeTouchHandler = new NodeTouchHandler<>(this, this::updatePinsPosition);

        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        onBackgroundInit();
    }

    protected void onBackgroundInit() {
        setBackground(new SelectableDrawable());
    }

    public String getNodeId() {
        return id;
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
        outputPins.add(pinRowView);
    }

    public void addInput(PinRowView pinRowView) {
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

    protected void invalidateBackground() {
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
    public @NotNull String getType() {
        return id;
    }

    @Override
    public int getNodeX() {
        return getLeft();
    }

    @Override
    public int getNodeY() {
        return getTop();
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
        var rowView = pin.createRowView();
        rowView.setText(name);

        addOutput(rowView);
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

    @Override
    public Object serialize() {
        var nodeData = new BPNodeData();
        nodeData.input = serialize(inputPins);
        nodeData.output = serialize(outputPins);

        return nodeData;
    }

    private Map<String, Object> serialize(List<PinRowView> pinRowViews) {
        return pinRowViews.stream().collect(
                HashMap::new,
                (m, row) -> {
                    var pin = row.getPinView().getPin();
                    var id = pin.getId().toString();

                    m.put(id, row.serialize());
                },
                HashMap::putAll
        );
    }

    private static class BPNodeData {
        public Map<String, Object> input;
        public Map<String, Object> output;
    }
}
