package com.rodev.jbpcore.blueprint.pin.map_pin;

import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.view.CompoundImageDrawable;
import com.rodev.jbpcore.view.TintedImage;
import icyllis.modernui.graphics.drawable.Drawable;

import static icyllis.modernui.view.View.dp;

public interface MapPin extends Pin {

    @Override
    default Drawable createDrawable() {
        var icon = getVariableType().getIcon();

        var img = icon.connected();
        var keyTypeImage = new TintedImage(img);
        var valueTypeImage = new TintedImage(img);

        keyTypeImage.setTint(~0);
        valueTypeImage.setTint(~0);

        var compoundDrawable = new CompoundImageDrawable(keyTypeImage, valueTypeImage, 0.3) {
            @Override
            public int getIntrinsicHeight() {
                return dp(24);
            }

            @Override
            public int getIntrinsicWidth() {
                return dp(24);
            }
        };

        onKeyTypeColorChangeListenerSet(newColor -> {
            compoundDrawable.getLeftImage().setTint(newColor);
            compoundDrawable.invalidateSelf();
        });

        onValueTypeColorChangeListenerSet(newColor -> {
            compoundDrawable.getRightImage().setTint(newColor);
            compoundDrawable.invalidateSelf();
        });

        return compoundDrawable;
    }

    default void onKeyTypeColorChangeListenerSet(ColorChangeListener listener) {}

    default void onValueTypeColorChangeListenerSet(ColorChangeListener listener) {}

    VariableType getKeyType();

    VariableType getValueType();

    @Override
    default int getColor() {
        return getKeyType().color();
    }

    interface ColorChangeListener {

        void setColor(int newColor);

    }
}
