package com.rodev.jbpcore.blueprint.pin.var_pin;

import com.rodev.jbpcore.blueprint.data.DataAccess;
import com.rodev.jbpcore.blueprint.data.action.PinType;
import com.rodev.jbpcore.blueprint.pin.*;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.graphics.drawable.StateListDrawable;
import icyllis.modernui.util.StateSet;

import static icyllis.modernui.view.View.dp;
import static icyllis.modernui.widget.CompoundButton.CHECKED_STATE_SET;

public interface VarPin extends Pin {

    @Override
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

    static Pin outputPin(PinType pinType) {
        return new OutVarPin(pinType);
    }

    static Pin inputPin(PinType pinType) {
        return new InVarPin(pinType);
    }
}
