package com.rodev.jbpcore.ui.pin;

import com.rodev.jbpcore.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.PinToggleListener;
import com.rodev.jbpcore.blueprint.pin.PositionSupplier;
import com.rodev.jbpcore.blueprint.pin.Positionable;
import com.rodev.jbpcore.blueprint.pin.dynamic.PinVariableTypeChangeListener;
import com.rodev.jbpcore.utils.ViewUtils;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.widget.CompoundButton;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PinView extends CompoundButton implements PinToggleListener, PositionSupplier, PinVariableTypeChangeListener {
    private final Pin pin;

    private boolean isDragging;

    private PinToggleListener pinToggleListener;

    public PinView(Pin pin) {
        this.pin = pin;

        pin.setPinToggleListener(this);
        pin.setPositionSupplier(this);

        pin.setVariableTypeChangedListener(this);

        setDrawableFromPin(pin);
    }

    @Override
    public void onVariableTypeChange(Pin pin, VariableType currentType, @Nullable VariableType newType) {
        setDrawableFromPin(pin);
    }

    private void setDrawableFromPin(Pin pin) {
        setButtonDrawable(pin.createDrawable());
        setButtonTintList(ColorStateList.valueOf(pin.getColor()));
    }

    public Pin getPin() {
        return pin;
    }

    private int[] currentCoordinates;

    @Override
    public boolean onHoverEvent(@NotNull MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_ENTER -> pin.onPinHoverStarted();
            case MotionEvent.ACTION_HOVER_MOVE -> pin.onPinHovered();
            case MotionEvent.ACTION_HOVER_EXIT -> pin.onPinHoverEnded();
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(@NotNull MotionEvent event) {
        var buttonPrimary = event.getButtonState() == MotionEvent.BUTTON_PRIMARY;

        var x = (int) event.getX();
        var y = (int) event.getY();

        if(isDragging && !buttonPrimary) {
            isDragging = false;
            onLineDrawEnd(x, y);
            return true;
        }

        if(!buttonPrimary) {
            return false;
        }

        var drag = event.getAction() == MotionEvent.ACTION_MOVE;
        var buttonUp = event.getAction() == MotionEvent.ACTION_UP;

        if(isDragging && buttonUp) {
            isDragging = false;
            onLineDrawEnd(x, y);
            return true;
        }

        if(!isDragging && drag) {
            currentCoordinates = ViewUtils.getRelativeViewPosition(this);
            isDragging = true;
            return true;
        }

        if(isDragging && drag) {
            pin.onDrag(
                    currentCoordinates[0],
                    currentCoordinates[1],
                    currentCoordinates[0] + x - getHalfWidth(),
                    currentCoordinates[1] + y - getHalfHeight()
            );
        }
        return true;
    }

    private void onLineDrawEnd(int x, int y) {
        pin.onDragEnd(
                currentCoordinates[0],
                currentCoordinates[1],
                currentCoordinates[0] + x - getHalfWidth(),
                currentCoordinates[1] + y - getHalfHeight()
        );
    }

    public void setPinConnectionListener(PinToggleListener pinToggleListener) {
        this.pinToggleListener = pinToggleListener;
    }

    private int getHalfWidth() {
        return getWidth() / 2;
    }

    private int getHalfHeight() {
        return getHeight() / 2;
    }

    @Override
    public int[] getPosition(Positionable object) {
        return ViewUtils.getRelativeViewPosition(this);
    }

    @Override
    public void onPinEnabled(Pin pin) {
        setChecked(true);

        if(pinToggleListener != null) {
            pinToggleListener.onPinEnabled(pin);
        }
    }

    @Override
    public void onPinDisabled(Pin pin) {
        setChecked(false);

        if(pinToggleListener != null) {
            pinToggleListener.onPinDisabled(pin);
        }
    }
}
