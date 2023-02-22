package com.rodev.test.blueprint.pin;

import com.rodev.test.blueprint.data.variable.VariableType;

import java.util.UUID;

public abstract class AbstractPin implements Pin {
    private final VariableType variableType;
    private final UUID id;
    private PinDragListener pinDragListener;
    private PinHoverListener pinHoverListener;
    private PinConnectionListener pinConnectionListener;
    private PinPositionSupplier pinPositionSupplier;

    private Position position;

    public AbstractPin(VariableType variableType) {
        this(variableType, UUID.randomUUID());
    }

    public AbstractPin(VariableType variableType, UUID uuid) {
        this.variableType = variableType;
        this.id = uuid;
    }

    @Override
    public VariableType getType() {
        return variableType;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setPinDragListener(PinDragListener pinDragListener) {
        this.pinDragListener = pinDragListener;
    }

    @Override
    public void setPinHoverListener(PinHoverListener pinHoverListener) {
        this.pinHoverListener = pinHoverListener;
    }

    @Override
    public void setPositionSupplier(PinPositionSupplier positionSupplier) {
        this.pinPositionSupplier = positionSupplier;
    }

    @Override
    public void onPositionUpdate() {
        if(pinPositionSupplier == null || position == null) return;

        var newPos = pinPositionSupplier.getPosition(this);

        position.set(newPos[0], newPos[1]);
    }

    @Override
    public Position getPosition() {
        if(position == null) {
            position = new Position();
            onPositionUpdate();
        }

        return position;
    }

    @Override
    public void setPinToggleListener(PinConnectionListener pinConnectionListener) {
        this.pinConnectionListener = pinConnectionListener;
    }

    @Override
    public void onLineDraw(int xStart, int yStart, int xEnd, int yEnd) {
        if (pinDragListener != null) {
            pinDragListener.onLineDrag(this, xStart, yStart, xEnd, yEnd);
        }
    }

    @Override
    public void onLineDrawEnd(int xStart, int yStart, int xEnd, int yEnd) {
        if (pinDragListener != null) {
            pinDragListener.onLineDragEnded(this, xStart, yStart, xEnd, yEnd);
        }
    }

    @Override
    public void enable() {
        if(pinConnectionListener == null) return;

        pinConnectionListener.onConnected(this);
    }

    @Override
    public void disable() {
        if(pinConnectionListener == null) return;

        pinConnectionListener.onDisconnected(this);
    }

    public boolean isTheSameTypeAs(Pin anotherPin) {
        return getType().type().equals(anotherPin.getType().type());
    }

    @Override
    public int getColor() {
        return variableType.color();
    }

    @Override
    public void onPinHovered() {
        pinHoverListener.onPinHovered(this);
    }

    @Override
    public void onPinHoverStarted() {
        pinHoverListener.onPinHoverStarted(this);
    }

    @Override
    public void onPinHoverEnded() {
        pinHoverListener.onPinHoverEnded(this);
    }
}
