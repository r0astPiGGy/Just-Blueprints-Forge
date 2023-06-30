package com.rodev.jbpcore.blueprint.graph;

import com.rodev.jbpcore.blueprint.pin.*;
import com.rodev.jbpcore.blueprint.pin.behaviour.ConnectionBehaviour;
import com.rodev.jbpcore.data.action.Action;
import com.rodev.jbpcore.blueprint.node.GraphNode;
import com.rodev.jbpcore.blueprint.node.NodeMoveListener;
import com.rodev.jbpcore.blueprint.node.NodePositionChangeListener;
import com.rodev.jbpcore.blueprint.node.NodeTouchListener;
import com.rodev.jbpcore.blueprint.pin.list_pin.ListPin;
import com.rodev.jbpcore.ui.contextmenu.ContextMenuBuilder;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.ViewGroup;

import java.util.HashSet;
import java.util.Set;

public class GraphControllerImpl implements
        GraphController, PinHoverListener, DragListener<Pin>, NodeTouchListener, NodeMoveListener, NodePositionChangeListener, PinConnectionHandler {

    private final Set<Pin> outputPins = new HashSet<>();
    private ViewHolder viewHolder;
    private ContextMenuBuilderProvider contextMenuBuilderProvider;
    private LineDrawCallback temporaryLineCallback = (c, paint) -> {};
    private Runnable invalidationCallback = () -> {};
    private Pin currentDraggingPin;
    private Pin currentHoveringPin;
    private GraphNode currentSelectedNode;
    private ViewMoveListener viewMoveListener;

    public GraphNode createViewAt(int x, int y, Action action) {
        var node = action.toNode(viewHolder.provideContext());

        createNodeAt(x, y, node);

        return node;
    }

    @Override
    public void createNodeAt(int x, int y, GraphNode node) {
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

    private boolean onSelectedNodeKeyPressed(GraphNode node, int keyCode, KeyEvent keyEvent) {
        if(keyEvent.getAction() != MotionEvent.ACTION_UP) return false;

        if(keyCode == KeyEvent.KEY_DELETE && currentSelectedNode == node) {
            handleOnNodeDelete(node);

            return true;
        }

        return false;
    }

    private void handleOnNodeDelete(GraphNode node) {
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
        outputPin.getConnectionBehaviour().getConnections().forEach(inputPin -> {
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
        paint.setStrokeWidth(7);
        paint.setColor(color);
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
    public void onDrag(Pin pin, int xStart, int yStart, int xEnd, int yEnd) {
        if(currentDraggingPin == null || currentDraggingPin != pin) {
            currentDraggingPin = pin;
        }

        if(pin.getConnectionBehaviour().isConnected() && !pin.getConnectionBehaviour().supportMultipleConnections()) {
            pin.disconnectAll();
        }

        drawTemporaryLineAt(xStart, yStart, xEnd, yEnd, pin.getColor());
    }

    @Override
    public void onDragEnded(Pin pin, int xStart, int yStart, int xEnd, int yEnd) {
        if (currentHoveringPin == null) {
            var builder = contextMenuBuilderProvider.createBuilder(xEnd, yEnd);
            handleContextMenuOpen(builder, pin);

            return;
        }

        clearLastTemporaryLine();

        pin.connect(currentHoveringPin);
    }

    private void handleContextMenuOpen(ContextMenuBuilder builder, Pin pin) {
        if(pin.getConnectionBehaviour().isInput()) {
            handleInputContextMenuOpen(builder, pin);
        }
        if(pin.getConnectionBehaviour().isOutput()) {
            handleOutputContextMenuOpen(builder, pin);
        }
    }

    private void handleOutputContextMenuOpen(ContextMenuBuilder builder, Pin pin) {
        var header = "Actions taking a ";

        if(pin instanceof ListPin list) {
            header += "list of " + list.getElementType().type();
        } else {
            header += pin.getVariableType().type();
        }

        builder
                .withHeader(header)
                .filtering(action -> !action.acceptsInputType(pin.getVariableType()))
                .onItemClick(action -> {
                    handleOnContextMenuItemClicked(action, builder.x, builder.y, pin);
                })
                .onDismiss(this::clearLastTemporaryLine)
                .show();
    }

    private void handleInputContextMenuOpen(ContextMenuBuilder builder, Pin pin) {
        var header = "Actions returning a ";

        if(pin instanceof ListPin list) {
            // TODO: add predicate
            header += "list of " + list.getElementType().type();
        } else {
            header += pin.getVariableType().type();
        }

        builder
                .withHeader(header)
                .filtering(action -> !action.acceptsOutputType(pin.getVariableType()))
                .onItemClick(action -> {
                    handleOnContextMenuItemClicked(action, builder.x, builder.y, pin);
                })
                .onDismiss(this::clearLastTemporaryLine)
                .show();
    }

    private void handleOnContextMenuItemClicked(Action action, final int x, final int y, Pin pin) {
        var node = createViewAt(x, y, action);

        node.setOnNodePreDrawCallback(createdNode -> {
            var found = createdNode.getFirstApplicablePinFor(pin);
            if(found == null) return true;

            var pos = found.getPosition();

            var nx = x - (pos.getX() - x);
            var ny = y - (pos.getY() - y);

            pin.connect(found);

            createdNode.moveTo(nx, ny);

            return false;
        });

        clearLastTemporaryLine();
    }

    @Override
    public boolean handleConnect(Pin target, Pin connection) {
        if(target == connection) return false;

        ConnectionBehaviour targetBehaviour = target.getConnectionBehaviour();

        if(!target.isApplicable(connection)) return false;

        if(targetBehaviour.isConnectedTo(connection)) return false;

        if(targetBehaviour.isConnected() && !targetBehaviour.supportMultipleConnections()) return false;
        
        target.setIsBeingConnected(true);

        if (connection.isBeingConnected()) {
            return true;
        }

        var connectResult = connection.connect(target);
        var failed = !connectResult;

        target.setIsBeingConnected(false);
        connection.setIsBeingConnected(false);

        if (failed) {
            return false;
        }

        if(target.getConnectionBehaviour().isOutput()) {
            outputPins.add(target);
        } else {
            outputPins.add(connection);
        }

        return true;
    }

    @Override
    public boolean handleDisconnect(Pin target, Pin connection) {
        if(target == connection) return false;
        
        if(!target.getConnectionBehaviour().isConnectedTo(connection)) return false;

        target.setIsBeingDisconnected(true);

        if (connection.isBeingDisconnected()) {
            return true;
        }

        var disconnectResult = connection.disconnect(target);
        var failed = !disconnectResult;

        target.setIsBeingDisconnected(false);
        connection.setIsBeingDisconnected(false);

        if (failed) {
            throw new IllegalStateException("Shouldn't get here.");
        }

        if(target.getConnectionBehaviour().isOutput()) {
            onOutputDisconnected(target);
        } else {
            onOutputDisconnected(connection);
        }

        return true;
    }

    private void onOutputDisconnected(Pin output) {
        if(output.getConnectionBehaviour().isConnected()) return;

        outputPins.remove(output);
    }

    @Override
    public boolean onDisconnectedAll(Pin target) {
        target.getConnectionBehaviour()
                .getConnections()
                .stream() // Avoid ConcurrentModificationException
                .toList()
                .forEach(target::disconnect);

        return true;
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
    public void onNodeTouch(GraphNode node, int x, int y) {
        if(isNodeSelected(node)) return;

        deselectCurrentSelectedNode();

        currentSelectedNode = node;
        currentSelectedNode.select();
        currentSelectedNode.asView().requestFocus();
    }

    @Override
    public void onMove(GraphNode node, int xStart, int yStart, int xEnd, int yEnd) {
        if(!isNodeSelected(node)) return;

        if(viewMoveListener != null) {
            moveTo(node, xEnd, yEnd);
        }
    }

    @Override
    public void moveTo(GraphNode node, int x, int y) {
        if(viewMoveListener != null) {
            viewMoveListener.onMove(node.asView(), x, y);
        }
    }

    @Override
    public void onGraphTouch(float x, float y) {
        deselectCurrentSelectedNode();
    }

    private boolean isNodeSelected(GraphNode node) {
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
