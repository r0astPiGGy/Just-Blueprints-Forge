package com.rodev.test.blueprint.pin;

import com.rodev.test.blueprint.node.BPNode;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.TextView;
import org.jetbrains.annotations.NotNull;

public class PinRowView extends LinearLayout {

    private final PinView pinView;
    private final TextView textView;

    public PinRowView(PinView pin, String text, Direction direction) {
        setOrientation(LinearLayout.HORIZONTAL);
        var textView = new TextView();
        textView.setText(text);
        setMinimumWidth(dp(150));
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (direction == Direction.LEFT) {
            setGravity(Gravity.CENTER | Gravity.START);
            addView(pin);
            addView(textView);
        } else {
            setGravity(Gravity.CENTER | Gravity.END);
            addView(textView);
            addView(pin);
        }
        this.textView = textView;
        pinView = pin;
    }

    public PinRowView setText(String text) {
        textView.setText(text);
        return this;
    }

    public PinView getPinView() {
        return pinView;
    }

    public static PinRowView leftDirectedRow(PinView pinView, String variableName) {
        return new PinRowView(pinView, variableName, Direction.LEFT);
    }

    public static PinRowView rightDirectedRow(PinView pinView, String variableName) {
        return new PinRowView(pinView, variableName, Direction.RIGHT);
    }

    public enum Direction {
        LEFT,
        RIGHT
    }

}
