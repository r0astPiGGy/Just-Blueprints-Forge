package com.rodev.jbpcore.blueprint.pin;

import com.rodev.jbpcore.blueprint.pin.behaviour.ConnectionBehaviour;
import com.rodev.jbpcore.blueprint.pin.dynamic.*;
import com.rodev.jbpcore.data.action.pin_type.PinType;

import java.util.UUID;

public class PinImpl implements Pin {
    private final PinType pinType;
    private UUID id;
    private DragListener<Pin> pinDragListener;
    private PinHoverListener pinHoverListener;
    private PinToggleListener pinToggleListener;
    private PositionSupplier positionSupplier;
    private PinConnectionHandler pinConnectionHandler;
    private PinConnectionListener pinConnectionListener;
    private PinVariableTypeChangeListener pinVariableTypeChangeListener;
    private final ConnectionBehaviour connectionBehaviour;
    private boolean isBeingDisconnected;
    private boolean isBeingConnected;
    private Position position;

    public PinImpl(PinType pinType, ConnectionBehaviour connectionBehaviour) {
        this(pinType, UUID.randomUUID(), connectionBehaviour);
    }

    public PinImpl(PinType pinType, UUID uuid, ConnectionBehaviour connectionBehaviour) {
        this.pinType = pinType;
        this.id = uuid;
        this.connectionBehaviour = connectionBehaviour;
    }

    @Override
    public boolean connect(Pin pin) {
        if(!handleOnConnect(pin)) return false;

        var behaviour = getConnectionBehaviour();
        boolean wasNotConnected = !behaviour.isConnected();
        behaviour.onPinConnected(pin);
        invokePinConnected(pin);

        if(wasNotConnected) {
            enable();
        }

        return true;
    }

    @Override
    public boolean disconnect(Pin pin) {
        if(!handleOnDisconnect(pin)) return false;

        var behaviour = getConnectionBehaviour();
        behaviour.onPinDisconnected(pin);
        invokePinDisconnected(pin);

        if(!behaviour.isConnected()) {
            disable();
        }

        return true;
    }

    @Override
    public boolean disconnectAll() {
        if(!handleOnDisconnectAll()) return false;

        getConnectionBehaviour().onAllPinsDisconnected();
        disable();

        return true;
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
    public void setDragListener(DragListener<Pin> dragListener) {
        this.pinDragListener = dragListener;
    }

    @Override
    public void setPinHoverListener(PinHoverListener pinHoverListener) {
        this.pinHoverListener = pinHoverListener;
    }

    @Override
    public void setPositionSupplier(PositionSupplier positionSupplier) {
        this.positionSupplier = positionSupplier;
    }

    @Override
    public void setPinConnectionHandler(PinConnectionHandler pinConnectionHandler) {
        this.pinConnectionHandler = pinConnectionHandler;
    }

    @Override
    public void setPinConnectionListener(PinConnectionListener pinConnectionListener) {
        this.pinConnectionListener = pinConnectionListener;
    }

    public void invokePinConnected(Pin connection) {
        if(pinConnectionListener == null) return;

        pinConnectionListener.onConnect(this, connection);
    }

    public void invokePinDisconnected(Pin connection) {
        if(pinConnectionListener == null) return;

        pinConnectionListener.onDisconnect(this, connection);
    }

    @Override
    public void setVariableTypeChangedListener(PinVariableTypeChangeListener listener) {
        pinVariableTypeChangeListener = listener;
    }

    @Override
    public void invokeVariableTypeChanged() {
        if(pinVariableTypeChangeListener == null) return;

        pinVariableTypeChangeListener.onVariableTypeChange(this, null, null);
    }

    protected boolean handleOnConnect(Pin connection) {
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
        if(positionSupplier == null || position == null) return;

        var newPos = positionSupplier.getPosition(this);

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
    public void onDrag(int xStart, int yStart, int xEnd, int yEnd) {
        if (pinDragListener != null) {
            pinDragListener.onDrag(this, xStart, yStart, xEnd, yEnd);
        }
    }

    @Override
    public void onDragEnd(int xStart, int yStart, int xEnd, int yEnd) {
        if (pinDragListener != null) {
            pinDragListener.onDragEnded(this, xStart, yStart, xEnd, yEnd);
        }
    }

    @Override
    public void enable() {
        if(pinToggleListener == null) return;

        pinToggleListener.onPinEnabled(this);
    }

    @Override
    public void disable() {
        if (pinToggleListener == null) return;

        pinToggleListener.onPinDisabled(this);
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

    @Override
    public ConnectionBehaviour getConnectionBehaviour() {
        return connectionBehaviour;
    }
}
