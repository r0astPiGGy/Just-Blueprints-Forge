package com.rodev.test.blueprint.pin;

import com.rodev.test.utils.ViewUtils;
import icyllis.modernui.R;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.util.StateSet;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.widget.CompoundButton;
import org.jetbrains.annotations.NotNull;

public class PinView extends CompoundButton implements PinToggleListener, PinPositionSupplier {

    private static final int[][] ENABLED_CHECKED_STATES = {
            new int[]{R.attr.state_enabled, R.attr.state_checked}, // [0]
            new int[]{R.attr.state_enabled, -R.attr.state_checked}, // [1]
            StateSet.WILD_CARD // [2]
    };

    private final Pin pin;

    private boolean isDragging;

    public PinView(Pin pin) {
        this.pin = pin;

        pin.setPinToggleListener(this);
        pin.setPositionSupplier(this);

        setButtonDrawable(pin.createDrawable());
        setButtonTintList(new ColorStateList(ENABLED_CHECKED_STATES, new int[] {pin.getColor(), pin.getColor(), pin.getColor()}));
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
            pin.onLineDraw(
                    currentCoordinates[0],
                    currentCoordinates[1],
                    currentCoordinates[0] + x - getHalfWidth(),
                    currentCoordinates[1] + y - getHalfHeight()
            );
        }
        return true;
    }

    private void onLineDrawEnd(int x, int y) {
        pin.onLineDrawEnd(
                currentCoordinates[0],
                currentCoordinates[1],
                currentCoordinates[0] + x - getHalfWidth(),
                currentCoordinates[1] + y - getHalfHeight()
        );
    }

    private int getHalfWidth() {
        return getWidth() / 2;
    }

    private int getHalfHeight() {
        return getHeight() / 2;
    }

    @Override
    public int[] getPosition(Pin pin) {
        return ViewUtils.getRelativeViewPosition(this);
    }

    @Override
    public void onEnable(Pin pin) {
        setChecked(true);
    }

    @Override
    public void onDisable(Pin pin) {
        setChecked(false);
    }
}
