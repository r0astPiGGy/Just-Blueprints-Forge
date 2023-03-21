package com.rodev.jbpcore.blueprint.pin;

import com.rodev.jbpcore.blueprint.data.action.PinType;

import java.util.UUID;

public abstract class AbstractPin implements Pin {
    private final PinType pinType;
    private UUID id;
    private PinDragListener pinDragListener;
    private PinHoverListener pinHoverListener;
    private PinToggleListener pinToggleListener;
    private PinPositionSupplier pinPositionSupplier;
    private PinConnectionHandler pinConnectionHandler;
    private boolean isBeingDisconnected;
    private boolean isBeingConnected;
    private Position position;

    public AbstractPin(PinType pinType) {
        this(pinType, UUID.randomUUID());
    }

    public AbstractPin(PinType pinType, UUID uuid) {
        this.pinType = pinType;
        this.id = uuid;
    }

    @Override
    public void setIsBeingConnected(boolean value) {
        isBeingConnected = value;
    }

    @Override
    public void setIsBeingDisconnected(boolean value) {
        isBeingDisconnected = value;
    }

    @Override
    public boolean isBeingConnected() {
        return isBeingConnected;
    }

    public boolean isBeingDisconnected() {
        return isBeingDisconnected;
    }

    @Override
    public PinType getType() {
        return pinType;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public void setId(UUID id) {
        this.id = id;
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
    public void setPinConnectionHandler(PinConnectionHandler pinConnectionHandler) {
        this.pinConnectionHandler = pinConnectionHandler;
    }

    protected boolean handleOnConnect(Pin connection) {
//        var connecting = isBeingConnected;
//
//        isBeingConnected = false;
//
//        if(shouldConnect && connecting) {
//            shouldConnect = false;
//            return true;
//        }

        if(pinConnectionHandler == null) return true;

        return pinConnectionHandler.handleConnect(this, connection);
    }

    protected boolean handleOnDisconnect(Pin connection) {
        if(pinConnectionHandler == null) return true;

        return pinConnectionHandler.handleDisconnect(this, connection);
    }

    protected boolean handleOnDisconnectAll() {
        if(pinConnectionHandler == null) return true;

        return pinConnectionHandler.onDisconnectedAll(this);
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
    public void setPinToggleListener(PinToggleListener pinToggleListener) {
        this.pinToggleListener = pinToggleListener;
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
        if(pinToggleListener == null) return;

        pinToggleListener.onPinEnabled(this);
    }

    @Override
    public void disable() {
        if(pinToggleListener == null) return;

        pinToggleListener.onPinDisabled(this);
    }

    public boolean isTheSameTypeAs(Pin anotherPin) {
        return getType().getVariableType().equals(anotherPin.getType().getVariableType());
    }

    @Override
    public int getColor() {
        return getType().getVariableType().color();
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
