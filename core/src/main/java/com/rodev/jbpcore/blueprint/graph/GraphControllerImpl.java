package com.rodev.jbpcore.blueprint.graph;

import com.rodev.jbpcore.blueprint.data.action.Action;
import com.rodev.jbpcore.blueprint.node.BPNode;
import com.rodev.jbpcore.blueprint.node.NodeMoveListener;
import com.rodev.jbpcore.blueprint.node.NodePositionChangeListener;
import com.rodev.jbpcore.blueprint.node.NodeTouchListener;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.PinDragListener;
import com.rodev.jbpcore.blueprint.pin.PinHoverListener;
import com.rodev.jbpcore.blueprint.pin.exec_pin.ExecPin;
import com.rodev.jbpcore.contextmenu.ContextMenuBuilder;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;

import java.util.HashSet;
import java.util.Set;

public class GraphControllerImpl implements
        GraphController, PinHoverListener, PinDragListener, NodeTouchListener, NodeMoveListener, NodePositionChangeListener {

    private final Set<Pin> outputPins = new HashSet<>();
    private ViewHolder viewHolder;
    private ContextMenuBuilderProvider contextMenuBuilderProvider;
    private LineDrawCallback temporaryLineCallback = (c, paint) -> {};
    private Runnable invalidationCallback = () -> {};
    private Pin currentDraggingPin;
    private Pin currentHoveringPin;
    private BPNode currentSelectedNode;
    private ViewMoveListener viewMoveListener;

    public void createViewAt(int x, int y, Action action) {
        var node = action.toNode();

        createNodeAt(x, y, node);
    }

    @Override
    public void createNodeAt(int x, int y, BPNode node) {
        node.setPinHoverListener(this);
        node.setPinDragListener(this);
        node.setNodeTouchListener(this);
        node.setNodeMoveListener(this);
        node.setNodePositionChangeListener(this);

        viewHolder.addViewAt(node.asView(), x, y);
    }

    @Override
    public void onContextMenuOpen(int x, int y) {
        contextMenuBuilderProvider.createBuilder(x, y)
                .withHeader("All actions for this Blueprint")
                .onItemClick(action -> {
                    createViewAt(x, y, action);
                })
                .onDismiss(this::clearLastTemporaryLine)
                .show();
    }

    @Override
    public void setViewMoveListener(ViewMoveListener viewMoveListener) {
        this.viewMoveListener = viewMoveListener;
    }

    @Override
    public void setViewHolder(ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    @Override
    public void setContextMenuBuilderProvider(ContextMenuBuilderProvider provider) {
        contextMenuBuilderProvider = provider;
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        temporaryLineCallback.draw(canvas, paint);
        outputPins.forEach(c -> drawAllConnections(c, canvas, paint));
    }

    private void drawAllConnections(Pin outputPin, Canvas canvas, Paint paint) {
        var outPosition = outputPin.getPosition();
        outputPin.getConnections().forEach(inputPin -> {
            var inputPosition = inputPin.getPosition();
            drawBezierLine(
                    canvas,
                    paint,
                    outPosition.getX(),
                    outPosition.getY(),
                    inputPosition.getX(),
                    inputPosition.getY(),
                    outputPin.getColor()
            );
        });
    }

    private void drawBezierLine(Canvas canvas, Paint paint, int xStart, int yStart, int xEnd, int yEnd, int color) {
        paint.setStrokeWidth(5);
        paint.setColor(color);
        paint.setSmoothRadius(100);
        int xHalf = (xEnd - xStart) / 2;
        int yHalf = (yEnd - yStart) / 2;
        canvas.drawBezier(xStart, yStart, xStart + xHalf, yStart, xStart + xHalf, yStart + yHalf, paint);
        //canvas.drawBezier(xStart + xHalf, yStart, xStart + xHalf, yStart + yHalf, xEnd, yEnd, paint);
        canvas.drawBezier(xEnd, yEnd, xEnd - xHalf, yEnd, xEnd - xHalf, yEnd - yHalf, paint);
    }

    @Override
    public void setInvalidationCallback(Runnable invalidationCallback) {
        this.invalidationCallback = invalidationCallback;
    }

    public void drawTemporaryLineAt(int xStart, int yStart, int xEnd, int yEnd, int color) {
        temporaryLineCallback = (canvas, paint) -> {
            drawBezierLine(canvas, paint, xStart, yStart, xEnd, yEnd, color);
        };
        invalidationCallback.run();
    }

    public void clearLastTemporaryLine() {
        temporaryLineCallback = (c, paint) -> {};
        invalidationCallback.run();
    }

    @Override
    public void onLineDrag(Pin pin, int xStart, int yStart, int xEnd, int yEnd) {
        if(currentDraggingPin == null || currentDraggingPin != pin) {
            currentDraggingPin = pin;
        }

        if(pin.isConnected() && !pin.supportMultipleConnections()) {
            disconnectSingleConnectionPin(pin);
        }

        drawTemporaryLineAt(xStart, yStart, xEnd, yEnd, pin.getColor());
    }

    @Override
    public void onLineDragEnded(Pin pin, int xStart, int yStart, int xEnd, int yEnd) {
        System.out.println("==========");
        System.out.println("trying to connect " + currentHoveringPin + " to " + pin);

        if (currentHoveringPin == null) {
            // Open context menu for applicable input/output types
            if(pin instanceof ExecPin) {
                onContextMenuOpen(xEnd, yEnd);
                return;
            }

            var builder = contextMenuBuilderProvider.createBuilder(xEnd, yEnd);
            handleContextMenuOpen(builder, pin);

            return;
        }

        clearLastTemporaryLine();

        if (!currentHoveringPin.isApplicable(pin)) return;

        if(pin.isInput() && currentHoveringPin.isOutput()) {
            handleInputConnection(pin, currentHoveringPin);
            return;
        }

        if(currentHoveringPin.isInput() && pin.isOutput()) {
            handleOutputConnection(currentHoveringPin, pin);
            return;
        }

    }

    private void handleContextMenuOpen(ContextMenuBuilder builder, Pin pin) {
        if(pin.isInput()) {
            handleInputContextMenuOpen(builder, pin);
        }
        if(pin.isOutput()) {
            handleOutputContextMenuOpen(builder, pin);
        }
    }

    private void handleOutputContextMenuOpen(ContextMenuBuilder builder, Pin pin) {
        builder
                .withHeader("Actions taking a " + pin.getType().getVariableType().type())
                .filtering(action -> !action.acceptsInputType(pin.getType().getVariableType()))
                .onItemClick(action -> {
                    createViewAt(builder.x, builder.y, action);
                    clearLastTemporaryLine();
                })
                .onDismiss(this::clearLastTemporaryLine)
                .show();
    }

    private void handleInputContextMenuOpen(ContextMenuBuilder builder, Pin pin) {
        builder
                .withHeader("Actions returning a " + pin.getType().getVariableType().type())
                .filtering(action -> !action.acceptsOutputType(pin.getType().getVariableType()))
                .onItemClick(action -> {
                    createViewAt(builder.x, builder.y, action);
                    clearLastTemporaryLine();
                })
                .onDismiss(this::clearLastTemporaryLine)
                .show();
    }

    private void handleInputConnection(Pin input, Pin output) {
        System.out.println("HANDLING INPUT CONNECTION");
        System.out.println("Input = " + input);
        System.out.println("Output = " + output);

        if(output.isPinConnected(input)) return;

        System.out.println("Pins aren't connected to each other, connecting them");

        connect(input, output);
    }

    private void handleOutputConnection(Pin input, Pin output) {
        System.out.println("HANDLING OUTPUT CONNECTION");
        System.out.println("Input = " + input);
        System.out.println("Output = " + output);

        if(input.isPinConnected(output)) return;

        System.out.println("Pins aren't connected to each other, checking if input is connected");

        if(input.isConnected()) {
            System.out.println("Input is connected, trying to disconnect it");
            disconnectSingleConnectionPin(input);
        }

        System.out.println("Connecting input and output pins");

        connect(input, output);
    }

    public void connect(Pin input, Pin output) {
        outputPins.add(output);

        input.connect(output);
        output.connect(input);

        System.out.println("Pins " + input + " and " + output + " connected");
    }

    private void disconnectSingleConnectionPin(Pin pin) {
        var outputPin = pin.getConnections().stream().findFirst().orElse(null);

        if(outputPin == null) throw new IllegalStateException();

        outputPin.disconnect(pin);
        if (!outputPin.isConnected()) {
            outputPins.remove(outputPin);
        }

        pin.disconnectAll();

        System.out.println("Pins " + pin + " and " + outputPin + " disconnected");
    }

    @Override
    public void onPinHoverStarted(Pin pin) {
        currentHoveringPin = pin;
    }

    @Override
    public void onPinHoverEnded(Pin pin) {
        currentHoveringPin = null;
    }

    @Override
    public void onNodeTouch(BPNode node, int x, int y) {
        if(isNodeSelected(node)) return;

        deselectCurrentSelectedNode();

        currentSelectedNode = node;
        currentSelectedNode.select();
    }

    @Override
    public boolean onMove(BPNode node, int xStart, int yStart, int xEnd, int yEnd) {
        if(!isNodeSelected(node)) return false;

        if(viewMoveListener != null) {
            moveTo(node, xEnd, yEnd);
            return true;
        }

        return false;
    }

    @Override
    public void moveTo(BPNode node, int x, int y) {
        if(viewMoveListener != null) {
            viewMoveListener.onMove(node.asView(), x, y);
        }
    }

    @Override
    public void onGraphTouch(float x, float y) {
        deselectCurrentSelectedNode();
    }

    private boolean isNodeSelected(BPNode node) {
        return currentSelectedNode == node;
    }

    private void deselectCurrentSelectedNode() {
        if(currentSelectedNode != null) {
            currentSelectedNode.deselect();
            currentSelectedNode = null;
        }
    }

    @FunctionalInterface
    interface LineDrawCallback {
        void draw(Canvas canvas, Paint paint);
    }
}
