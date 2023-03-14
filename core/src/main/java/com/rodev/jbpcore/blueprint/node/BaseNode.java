package com.rodev.jbpcore.blueprint.node;

import com.rodev.jbpcore.blueprint.ChildRoot;
import com.rodev.jbpcore.blueprint.data.variable.VariableTypeRegistry;
import com.rodev.jbpcore.blueprint.pin.*;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public abstract class BaseNode extends LinearLayout implements BPNode, PinHoverListener, PinDragListener {

    private Boolean selected = false;

    // TODO: Remove, find another solution due to huge memory consume
    private final Map<String, PinRowView> outputPinsByName = new HashMap<>();
    private final Map<String, PinRowView> inputPinsByName = new HashMap<>();
    private final List<PinRowView> outputPins = new LinkedList<>();
    private final List<PinRowView> inputPins = new LinkedList<>();
    private final String id;
    private final NodeTouchHandler<BaseNode> nodeTouchHandler;

    private NodePositionChangeListener nodePositionChangeListener;
    @Setter
    private PinHoverListener pinHoverListener;
    @Setter
    private PinDragListener pinDragListener;

    public BaseNode(String id) {
        this.id = id;

        nodeTouchHandler = new NodeTouchHandler<>(this, this::updatePinsPosition);

        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        onBackgroundInit();
    }

    @Override
    public View asView() {
        return this;
    }
    @Override
    public void onPinHovered(Pin pin) {
        pinHoverListener.onPinHovered(pin);
    }

    @Override
    public void onPinHoverEnded(Pin pin) {
        pinHoverListener.onPinHoverEnded(pin);
    }

    @Override
    public void onPinHoverStarted(Pin pin) {
        pinHoverListener.onPinHoverStarted(pin);
    }

    @Override
    public void onLineDrag(Pin pin, int xStart, int yStart, int xEnd, int yEnd) {
        pinDragListener.onLineDrag(pin, xStart, yStart, xEnd, yEnd);
    }

    @Override
    public void onLineDragEnded(Pin pin, int xStart, int yStart, int xEnd, int yEnd) {
        pinDragListener.onLineDragEnded(pin, xStart, yStart, xEnd, yEnd);
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

    protected void addOutput(PinRowView pinRowView) {
        outputPins.add(pinRowView);
    }

    protected void addInput(PinRowView pinRowView) {
        inputPins.add(pinRowView);
    }

    @Override
    public void setNodePositionChangeListener(NodePositionChangeListener listener) {
        nodePositionChangeListener = listener;
    }

    @Override
    public void moveTo(int x, int y) {
        nodePositionChangeListener.moveTo(this, x, y);
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

        pin.setPinHoverListener(this);
        pin.setPinDragListener(this);
        inputPinsByName.put(pin.getType().getId(), rowView);

        addInput(rowView);
    }

    @Override
    public void addOutputPin(Pin pin, String name) {
        var rowView = pin.createRowView();
        rowView.setText(name);

        pin.setPinHoverListener(this);
        pin.setPinDragListener(this);
        outputPinsByName.put(pin.getType().getId(), rowView);

        addOutput(rowView);
    }

    @Override
    public void forEachInputPin(Consumer<Pin> pinConsumer) {
        inputPins.forEach(row -> pinConsumer.accept(row.getPinView().getPin()));
    }

    @Override
    public void forEachOutputPin(Consumer<Pin> pinConsumer) {
        outputPins.forEach(row -> pinConsumer.accept(row.getPinView().getPin()));
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

    private Map<String, PinData> serialize(List<PinRowView> pinRowViews) {
        return pinRowViews.stream().collect(
                HashMap::new,
                (m, row) -> {
                    var pin = row.getPinView().getPin();
                    var id = pin.getId().toString();

                    m.put(id, serialize(row));
                },
                HashMap::putAll
        );
    }

    public PinData serialize(PinRowView row) {
        var pinData = new PinData();

        var pin = row.getPinView().getPin();

        pinData.name = pin.getType().getId();

        if(pin.isInput() && pin.isConnected()) {
            pinData.connectedTo = ((InputPin) pin).getConnections().get(0).getId().toString();
        }
        pinData.value = row.getDefaultValue();
        pinData.type = pin.getType().getType();

        return pinData;
    }

    @Override
    public NodeDeserializer getDeserializer(Object data) {
        return new Deserializer(data, this);
    }

    // TODO: Move serialization/deserialization to WorkspaceImpl.java
    @RequiredArgsConstructor
    @Getter
    public class Deserializer implements NodeDeserializer {
        public final List<PinConnection> pinConnections = new LinkedList<>();
        public final Map<String, Pin> outputPins = new HashMap<>();
        private final Object data;
        private final BPNode node;

        public void deserialize() {
            //noinspection unchecked
            var nodeData = (Map<Object, Map<Object,Object>>) data;

            deserializeInput(nodeData.get("input"));
            deserializeOutput(nodeData.get("output"));
        }

        void deserializeInput(Map<Object, Object> pins) {
            if(pins.isEmpty()) return;

            pins.forEach((id, pinData) -> {
                //noinspection unchecked
                deserializeInputPin((String) id, (Map<Object, Object>) pinData);
            });
        }

        void deserializeInputPin(String id, Map<Object, Object> pinData) {
            String localId = (String) pinData.get("name");
            Object connectedTo = pinData.get("connectedTo");
            Object value = pinData.get("value");

            var pinRowView = inputPinsByName.get(localId);

            if(pinRowView == null) {
                throw new IllegalStateException("Not found PinRowView by name " + localId);
            }

            var pin = pinRowView.getPinView().getPin();
            pin.setId(UUID.fromString(id));

            if(value != null) {
                pinRowView.setDefaultValue(String.valueOf(value));
            }

            if(connectedTo != null) {
                var connectedToId = String.valueOf(connectedTo);
                pinConnections.add(new PinConnection(node, connectedToId, pin));
            }
        }

        void deserializeOutput(Map<Object, Object> pins) {
            if(pins.isEmpty()) return;

            pins.forEach((id, pinData) -> {
                //noinspection unchecked
                deserializeOutputPin((String) id, (Map<Object, Object>) pinData);
            });
        }

        void deserializeOutputPin(String id, Map<Object, Object> pinData) {
            String localId = (String) pinData.get("name");
            Object value = pinData.get("value");

            var pinRowView = outputPinsByName.get(localId);
            var pin = pinRowView.getPinView().getPin();
            pin.setId(UUID.fromString(id));

            outputPins.put(id, pin);

            if(value != null) {
                pinRowView.setDefaultValue(String.valueOf(value));
            }
        }
    }

    private static class BPNodeData {
        public Map<String, PinData> input;
        public Map<String, PinData> output;
    }

    private static class PinData {
        public String name;
        public String connectedTo;
        public String value;
        public String type;

    }
}
