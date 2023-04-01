package com.rodev.jbpcore.blueprint.pin;

import com.rodev.jbpcore.blueprint.pin.dynamic.PinVariableTypeChangeListener;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicPinType;
import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class AbstractPin implements Pin {
    private final PinType pinType;
    private UUID id;
    private PinDragListener pinDragListener;
    private PinHoverListener pinHoverListener;
    private PinToggleListener pinToggleListener;
    private PositionSupplier positionSupplier;
    private PinConnectionHandler pinConnectionHandler;
    private PinConnectionListener pinConnectionListener;
    private PinVariableTypeChangeListener pinVariableTypeChangeListener;
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
    public boolean isDynamic() {
        return getType().isDynamic();
    }

    @Override
    public void setVariableType(VariableType variableType) {
        doIfDynamic(dynamicPinType -> dynamicPinType.setVariableType(variableType));
    }

    @Override
    public void resetVariableType() {
        doIfDynamic(DynamicPinType::resetVariableType);
    }

    @Override
    public boolean isDynamicVariableSet() {
        return returnIfDynamic(DynamicPinType::isDynamicVariableTypeSet);
    }

    @Override
    public @Nullable String getDependantId() {
        return returnIfDynamic(DynamicPinType::getDependsOn);
    }

    @Override
    public @NotNull VariableType getDependantType(@NotNull Pin dependantPin) {
        return returnIfDynamic(d -> d.resolveVariableTypeInPin(dependantPin));
    }

    private void doIfDynamic(Consumer<DynamicPinType> ifDynamicAction) {
        ifDynamicAction.accept(requireDynamicPinType());
    }

    private <T> T returnIfDynamic(Function<DynamicPinType, T> ifDynamicFunction) {
        return ifDynamicFunction.apply(requireDynamicPinType());
    }

    private DynamicPinType requireDynamicPinType() {
        if(pinType instanceof DynamicPinType dynamicPinType) {
            return dynamicPinType;
        }

        throw new IllegalStateException("This pin isn't dynamic");
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
        var type = getType().getVariableType();
        var anotherType = anotherPin.getType().getVariableType();

        return type.equals(anotherType);
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
