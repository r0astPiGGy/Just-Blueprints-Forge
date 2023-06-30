package com.rodev.jbpcore.blueprint.pin.map_pin;

import com.rodev.jbpcore.blueprint.pin.PinImpl;
import com.rodev.jbpcore.blueprint.pin.behaviour.ConnectionBehaviour;
import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.ui.drawable.CompoundImageDrawable;
import com.rodev.jbpcore.ui.view.TintedImage;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.view.View;

public class MapPin extends PinImpl {

    private final VariableType keyType;
    private final VariableType valueType;

    protected ColorChangeListener keyColorChangeListener;
    protected ColorChangeListener valueColorChangeListener;

    protected MapPin(MapPinType pinType, ConnectionBehaviour connectionBehaviour) {
        super(pinType, connectionBehaviour);

        this.valueType = pinType.getValueType();
        this.keyType = pinType.getKeyType();
    }

    public static MapPin inputPin(MapPinType pinType) {
        return new MapPin(pinType, new InputMapBehaviour());
    }

    public static MapPin outputPin(MapPinType pinType) {
        return new MapPin(pinType, new OutputMapBehaviour());
    }

    public static MapPin castToMapOrNull(Pin pin) {
        if (pin instanceof MapPin mapPin)
            return mapPin;

        return null;
    }

    @Override
    public Drawable createDrawable(View contextHolder) {
        var icon = getVariableType().getIcon();

        var img = icon.connected();
        var keyTypeImage = new TintedImage(img);
        var valueTypeImage = new TintedImage(img);

        keyTypeImage.setTint(~0);
        valueTypeImage.setTint(~0);

        var compoundDrawable = new CompoundImageDrawable(keyTypeImage, valueTypeImage, 0.3) {
            @Override
            public int getIntrinsicHeight() {
                return contextHolder.dp(24);
            }

            @Override
            public int getIntrinsicWidth() {
                return contextHolder.dp(24);
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

    private void onKeyTypeColorChangeListenerSet(ColorChangeListener listener) {
        keyColorChangeListener = listener;
        updateKeyColor();
    }

    private void onValueTypeColorChangeListenerSet(ColorChangeListener listener) {
        valueColorChangeListener = listener;
        updateValueColor();
    }

    protected void updateKeyColor() {
        keyColorChangeListener.setColor(getKeyType().color());
    }

    protected void updateValueColor() {
        valueColorChangeListener.setColor(getValueType().color());
    }

    public VariableType getKeyType() {
        return keyType;
    }

    public VariableType getValueType() {
        return valueType;
    }

    @Override
    public int getColor() {
        return getKeyType().color();
    }

    interface ColorChangeListener {

        void setColor(int newColor);

    }
}
