package com.rodev.test.blueprint.graph;

import com.rodev.test.Colors;
import com.rodev.test.blueprint.BPViewPort;
import com.rodev.test.blueprint.data.DataAccess;
import com.rodev.test.blueprint.data.action.Action;
import com.rodev.test.blueprint.node.BPNode;
import com.rodev.test.blueprint.node.NodeMoveListener;
import com.rodev.test.blueprint.node.NodeTouchListener;
import com.rodev.test.blueprint.pin.Pin;
import com.rodev.test.blueprint.pin.PinDragListener;
import com.rodev.test.blueprint.pin.PinHoverListener;
import com.rodev.test.contextmenu.BlueprintMenuPopup;
import com.rodev.test.contextmenu.ContextMenuItem;
import com.rodev.test.contextmenu.ContextMenuItemImpl;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.view.View;

import java.util.HashSet;
import java.util.Set;

public class GraphControllerImpl implements
        GraphController, PinHoverListener, PinDragListener, DrawListener, NodeTouchListener, GraphTouchListener, NodeMoveListener {

    private final BPViewPort viewPort;
    private final Set<Pin> outputPins = new HashSet<>();
    private LineDrawCallback lineDrawCallback = (c, paint) -> {};
    private Runnable invalidationCallback = () -> {};
    private Pin currentDraggingPin;
    private Pin currentHoveringPin;
    private BPNode currentSelectedNode;
    private ViewMoveListener viewMoveListener;

    public GraphControllerImpl(BPViewPort viewPort) {
        this.viewPort = viewPort;
    }

    @Override
    public View createViewAt(int x, int y, Action action) {
        return (View) action.toNode(pin -> {
            pin.setPinHoverListener(this);
            pin.setPinDragListener(this);
        }, node -> {
            node.setNodeTouchListener(this);
            node.setNodeMoveListener(this);
        });
    }

    @Override
    public void navigateTo(int x, int y) {
        var offsetX = viewPort.getWidth() / 2;
        var offsetY = viewPort.getHeight() / 2;
        viewPort.scrollTo(x - offsetX, y - offsetY);
    }

    @Override
    public void setViewMoveListener(ViewMoveListener viewMoveListener) {
        this.viewMoveListener = viewMoveListener;
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
        lineDrawCallback = (canvas, paint) -> {
            drawBezierLine(canvas, paint, xStart, yStart, xEnd, yEnd, color);
        };
        invalidationCallback.run();
    }

    public void clearLastTemporaryLine() {
        lineDrawCallback = (c, paint) -> {};
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
        clearLastTemporaryLine();

        if (currentHoveringPin == null) return;

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

    private void connect(Pin input, Pin output) {
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
    public void onDraw(Canvas canvas, Paint paint) {
        lineDrawCallback.draw(canvas, paint);
        outputPins.forEach(c -> drawAllConnections(c, canvas, paint));
    }

    @Override
    public void onNodeTouch(BPNode node, int x, int y) {
        if(isNodeSelected(node)) return;

        deselectCurrentSelectedNode();

        currentSelectedNode = node;
        currentSelectedNode.select();
    }

    @Override
    public <T extends View & BPNode> boolean onMove(T node, int xStart, int yStart, int xEnd, int yEnd) {
        if(!isNodeSelected(node)) return false;

//        xEnd = (int) ((double) xEnd / 40) * 40;
//        yEnd = (int) ((double) yEnd / 40) * 40;
//        if(xStart == xEnd && yStart == yEnd) return false;

        if(viewMoveListener != null) {
            viewMoveListener.onMove(node, xEnd, yEnd);
            return true;
        }

        return false;
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
