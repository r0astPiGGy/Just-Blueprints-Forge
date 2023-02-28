package com.rodev.test.blueprint.pin;

import com.rodev.test.blueprint.data.variable.DefaultInputValue;
import com.rodev.test.blueprint.node.BPNode;
import com.rodev.test.utils.TextViewCreationListener;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.TextView;
import org.jetbrains.annotations.NotNull;

public class PinRowView extends LinearLayout implements PinConnectionListener {

    private final PinView pinView;
    private final TextView textView;
    // TODO: Rename
    private final LinearLayout pinInfo = new LinearLayout();

    private DefaultInputValue defaultInputValue;

    public PinRowView(PinView pin, String text, Direction direction) {
        setOrientation(LinearLayout.HORIZONTAL);

        pin.setPinConnectionListener(this);

        var textView = new TextView();
        textView.setText(text);

        TextViewCreationListener.onPinTextCreated(textView);

        setMinimumWidth(dp(100));
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        pinInfo.setOrientation(VERTICAL);
        pinInfo.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        pinInfo.addView(textView);

        // TODO: Split to separate classes
        if (direction == Direction.LEFT) {
            setGravity(Gravity.CENTER | Gravity.START);
            addView(pin);
            addView(pinInfo);
        } else {
            setGravity(Gravity.CENTER | Gravity.END);
            addView(pinInfo);
            addView(pin);
        }

        this.textView = textView;
        pinView = pin;
    }

    public PinRowView setText(String text) {
        textView.setText(text);
        return this;
    }

    public void addDefaultValueView(DefaultInputValue defaultInputValue) {
        this.defaultInputValue = defaultInputValue;
        pinInfo.addView(defaultInputValue.asView());
    }

    public PinView getPinView() {
        return pinView;
    }

    @Override
    public void onConnected(Pin pin) {
        if(defaultInputValue == null) return;

        System.out.println("Default value hidden");
        defaultInputValue.asView().setVisibility(GONE);
    }

    @Override
    public void onDisconnected(Pin pin) {
        if(defaultInputValue == null) return;

        System.out.println("Default value shown");
        defaultInputValue.asView().setVisibility(VISIBLE);
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
