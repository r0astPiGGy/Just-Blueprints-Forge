package com.rodev.jbpcore.blueprint.graph;

import com.rodev.jbpcore.blueprint.data.action.Action;
import com.rodev.jbpcore.blueprint.node.BPNode;
import com.rodev.jbpcore.blueprint.node.NodeMoveListener;
import com.rodev.jbpcore.blueprint.node.NodePositionChangeListener;
import com.rodev.jbpcore.blueprint.node.NodeTouchListener;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.PinConnectionHandler;
import com.rodev.jbpcore.blueprint.pin.PinDragListener;
import com.rodev.jbpcore.blueprint.pin.PinHoverListener;
import com.rodev.jbpcore.blueprint.pin.exec_pin.ExecPin;
import com.rodev.jbpcore.contextmenu.ContextMenuBuilder;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.ViewGroup;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class GraphControllerImpl implements
        GraphController, PinHoverListener, PinDragListener, NodeTouchListener, NodeMoveListener, NodePositionChangeListener, PinConnectionHandler {

    private final Set<Pin> outputPins = new HashSet<>();
    private ViewHolder viewHolder;
    private ContextMenuBuilderProvider contextMenuBuilderProvider;
    private LineDrawCallback temporaryLineCallback = (c, paint) -> {};
    private Runnable invalidationCallback = () -> {};
    private Pin currentDraggingPin;
    private Pin currentHoveringPin;
    private BPNode currentSelectedNode;
    private ViewMoveListener viewMoveListener;

    public BPNode createViewAt(int x, int y, Action action) {
        var node = action.toNode();

        createNodeAt(x, y, node);

        return node;
    }

    @Override
    public void createNodeAt(int x, int y, BPNode node) {
        node.setPinHoverListener(this);
        node.setPinDragListener(this);
        node.setNodeTouchListener(this);
        node.setNodeMoveListener(this);
        node.setPinConnectionHandler(this);
        node.setNodePositionChangeListener(this);
        node.asView().setOnKeyListener((v, keyCode, event) -> {
            return onSelectedNodeKeyPressed(node, keyCode, event);
        });

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

    private boolean onSelectedNodeKeyPressed(BPNode node, int keyCode, KeyEvent keyEvent) {
        if(keyEvent.getAction() != MotionEvent.ACTION_UP) return false;

        if(keyCode == KeyEvent.KEY_DELETE && currentSelectedNode == node) {
            handleOnNodeDelete(node);

            return true;
        }

        return false;
    }

    private void handleOnNodeDelete(BPNode node) {
        var nodeView = node.asView();

        node.forEachPin(Pin::disconnectAll);

        if(nodeView.getParent() instanceof ViewGroup parent) {
            parent.removeView(nodeView);
            return;
        }

        throw new IllegalStateException("Shouldn't get here.");
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
            pin.disconnectAll();
        }

        drawTemporaryLineAt(xStart, yStart, xEnd, yEnd, pin.getColor());
    }

    @Override
    public void onLineDragEnded(Pin pin, int xStart, int yStart, int xEnd, int yEnd) {
        System.out.println("==========");
        System.out.println("trying to connect " + currentHoveringPin + " to " + pin);

        if (currentHoveringPin == null) {
//            // Open context menu for applicable input/output types
//            if(pin instanceof ExecPin) {
//                onContextMenuOpen(xEnd, yEnd);
//                return;
//            }

            var builder = contextMenuBuilderProvider.createBuilder(xEnd, yEnd);
            handleContextMenuOpen(builder, pin);

            return;
        }

        clearLastTemporaryLine();

        connect(pin, currentHoveringPin);
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
                    handleOnContextMenuItemClicked(action, builder.x, builder.y, pin);
                })
                .onDismiss(this::clearLastTemporaryLine)
                .show();
    }

    private void handleInputContextMenuOpen(ContextMenuBuilder builder, Pin pin) {
        builder
                .withHeader("Actions returning a " + pin.getType().getVariableType().type())
                .filtering(action -> !action.acceptsOutputType(pin.getType().getVariableType()))
                .onItemClick(action -> {
                    handleOnContextMenuItemClicked(action, builder.x, builder.y, pin);
                })
                .onDismiss(this::clearLastTemporaryLine)
                .show();
    }

    private void handleOnContextMenuItemClicked(Action action, final int x, final int y, Pin pin) {
        var node = createViewAt(x, y, action);

        node.setOnNodeCreatedCallback(createdNode -> {
            var found = createdNode.getFirstApplicablePinFor(pin);
            if(found == null) return;

            var pos = found.getPosition();

            var nx = x - (pos.getX() - x);
            var ny = y - (pos.getY() - y);

            connect(pin, found);

            // TODO fix: not updated
            createdNode.moveTo(nx, ny);
        });

        clearLastTemporaryLine();
    }

    @Override
    public boolean handleConnect(Pin pin, Pin connection) {
        if(pin == connection) return false;

        if(!pin.isApplicable(connection)) return false;

        if(pin.isPinConnected(connection)) return false;

        if(pin.isOutput()) {
            outputPins.add(pin);
        } else {
            outputPins.add(connection);
        }

        return true;
    }

    @Override
    public boolean handleDisconnect(Pin target, Pin connection) {
        if(!target.isPinConnected(connection)) return false;

        target.setIsBeingDisconnected(true);

        if (connection.isBeingDisconnected() || !connection.disconnect(target)) {
            return true;
        }

        target.setIsBeingDisconnected(false);
        connection.setIsBeingDisconnected(false);

        if(target.isOutput()) {
            onOutputDisconnected(target);
        } else {
            onOutputDisconnected(connection);
        }

        return true;
    }

    private void onOutputDisconnected(Pin output) {
        if(output.isConnected()) return;

        outputPins.remove(output);
    }

    @Override
    public boolean onDisconnectedAll(Pin target) {
        target.getConnections()
                .stream() // Avoid ConcurrentModificationException
                .toList()
                .forEach(target::disconnect);

        return true;
    }

    public void connect(Pin pin, Pin connection) {
        if (!pin.connect(connection)) {
            return;
        }

        if(connection.isInput()) {
            connection.disconnectAll();
        }

        if(!connection.connect(pin)) {
            throw new IllegalStateException("Couldn't get here.");
        }
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
        currentSelectedNode.asView().requestFocus();
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
