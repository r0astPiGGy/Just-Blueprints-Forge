package com.rodev.jbpcore.blueprint.pin;

import com.rodev.jbpcore.blueprint.pin.behaviour.ConnectionBehaviour;
import com.rodev.jbpcore.data.action.pin_type.PinType;
import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.dynamic.PinVariableTypeChangeListener;

// TODO не должно зависеть от фреймворка
import icyllis.modernui.graphics.drawable.Drawable;

import java.util.UUID;

// TODO перевести в class
public interface Pin extends Hoverable, Draggable<Pin>, Positionable, Toggleable {

    UUID getId();

    void setId(UUID id);

    /**
     * @deprecated метод возвращает объект класса фреймворка, что в контексте интерфейса недопустимо
     */
    @Deprecated
    default Drawable createDrawable() {
        var icon = getVariableType().getIcon();
        return PinDrawable.create(icon.icon(), icon.connected());
    }

    ConnectionBehaviour getConnectionBehaviour();

    PinType getType();

    default int getColor() {
        return getVariableType().color();
    }

    default String getLabel() {
        return getType().getName();
    }

    default String getLocalId() {
        return getType().getId();
    }

    default VariableType getVariableType() {
        return getType().getVariableType();
    }

    void setVariableTypeChangedListener(PinVariableTypeChangeListener listener);

    void invokeVariableTypeChanged();

    default boolean isApplicable(Pin anotherPin) {
        return getConnectionBehaviour().isApplicable(this, anotherPin);
    }

    default boolean isTheSameTypeAs(Pin anotherPin) {
        var type = getVariableType();
        var anotherType = anotherPin.getVariableType();

        return type.equals(anotherType);
    }

    // region Connectable

    void setPinConnectionHandler(PinConnectionHandler pinConnectionHandler);

    void setPinConnectionListener(PinConnectionListener pinConnectionListener);

    boolean connect(Pin pin);

    boolean disconnect(Pin pin);

    boolean disconnectAll();

    void setIsBeingConnected(boolean value);
    
    boolean isBeingConnected();
    
    void setIsBeingDisconnected(boolean value);

    boolean isBeingDisconnected();

    // endregion
}
