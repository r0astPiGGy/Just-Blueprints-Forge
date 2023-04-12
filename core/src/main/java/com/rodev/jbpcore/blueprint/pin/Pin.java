package com.rodev.jbpcore.blueprint.pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.dynamic.Dynamic;
import com.rodev.jbpcore.blueprint.pin.dynamic.PinVariableTypeChangeListener;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.graphics.drawable.StateListDrawable;
import icyllis.modernui.util.StateSet;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

import static icyllis.modernui.view.View.dp;
import static icyllis.modernui.widget.CompoundButton.CHECKED_STATE_SET;

public interface Pin extends Hoverable, Draggable, Positionable, Toggleable {

    UUID getId();

    void setId(UUID id);

    PinRowView createRowView();

    default Drawable createDrawable() {
        var icon = getVariableType().getIcon();
        return PinDrawable.create(icon.icon(), icon.connected());
    }

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

    boolean isInput();

    boolean isOutput();

    // region Connectable
    boolean isApplicable(Pin another);

    void setPinConnectionHandler(PinConnectionHandler pinConnectionHandler);

    void setPinConnectionListener(PinConnectionListener pinConnectionListener);

    Collection<Pin> getConnections();

    @Nullable
    Pin getFirstConnectedPin();

    boolean connect(Pin pin);

    boolean disconnect(Pin pin);

    boolean disconnectAll();

    boolean supportMultipleConnections();

    boolean isConnected();

    boolean isConnectedTo(Pin pin);

    void setIsBeingConnected(boolean value);
    
    boolean isBeingConnected();
    
    void setIsBeingDisconnected(boolean value);

    boolean isBeingDisconnected();

    // endregion
}
