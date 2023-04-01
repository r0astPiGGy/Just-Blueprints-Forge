package com.rodev.jbpcore.blueprint.pin;

import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.pin.dynamic.Dynamic;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.graphics.drawable.StateListDrawable;
import icyllis.modernui.util.StateSet;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

import static icyllis.modernui.view.View.dp;
import static icyllis.modernui.widget.CompoundButton.CHECKED_STATE_SET;

public interface Pin extends Dynamic, Hoverable, Draggable, Positionable, Toggleable {

    UUID getId();

    void setId(UUID id);

    PinRowView createRowView();

    default Drawable createDrawable() {
        var icon = getType().getVariableType().getIcon();
        StateListDrawable drawable = new StateListDrawable() {
            @Override
            public int getIntrinsicHeight() {
                return dp(24);
            }

            @Override
            public int getIntrinsicWidth() {
                return dp(24);
            }
        };

        drawable.addState(CHECKED_STATE_SET, new ImageDrawable(icon.connected()));
        drawable.addState(StateSet.WILD_CARD, new ImageDrawable(icon.icon()));
//        drawable.setEnterFadeDuration(300);
//        drawable.setExitFadeDuration(300);
        return drawable;
    }

    PinType getType();

    default int getColor() {
        return getType().getVariableType().color();
    }

    boolean isApplicable(Pin another);

    boolean isInput();

    boolean isOutput();

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

}
