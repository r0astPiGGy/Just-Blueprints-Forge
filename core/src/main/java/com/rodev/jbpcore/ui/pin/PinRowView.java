package com.rodev.jbpcore.ui.pin;

import com.rodev.jbpcore.data.variable.DefaultInputValue;
import com.rodev.jbpcore.blueprint.pin.Pin;
import com.rodev.jbpcore.blueprint.pin.PinToggleListener;
import com.rodev.jbpcore.utils.ParamsBuilder;
import com.rodev.jbpcore.handlers.TextViewCreationListener;
import icyllis.modernui.core.Context;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.TextView;

// TODO: Переделать под RelativeLayout
public class PinRowView extends LinearLayout implements PinToggleListener {

    private final PinView pinView;
    private final TextView textView;
    // TODO: Rename
    private final LinearLayout pinInfo;

    private DefaultInputValue defaultInputValue;

    public PinRowView(Context context, PinView pin, String text, Direction direction) {
        super(context);
        setOrientation(LinearLayout.HORIZONTAL);

        pinInfo = new LinearLayout(context);

        pin.setPinConnectionListener(this);

        var textView = new TextView(context);
        textView.setText(text);

        setPadding(0, dp(1.5f), 0, dp(1.5f));
        ParamsBuilder.using(LinearLayout.LayoutParams::new)
                .wrapContent()
                .setup(p -> {
                    p.rightMargin = dp(3);
                    p.leftMargin = dp(3);
                })
                .applyTo(pin);

        TextViewCreationListener.onPinTextCreated(textView);

        enableMinimumWidth();
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
    public void onPinEnabled(Pin pin) {
        if(defaultInputValue == null) return;
        defaultInputValue.hide();
    }

    @Override
    public void onPinDisabled(Pin pin) {
        if(defaultInputValue == null) return;
        defaultInputValue.show();
    }

    public String getDefaultValue() {
        if (defaultInputValue == null) {
            return null;
        }

        return defaultInputValue.getDefaultValue();
    }

    public void setDefaultValue(String value) {
        if(defaultInputValue == null) return;

        defaultInputValue.setDefaultValue(value);
    }

    public void enableMinimumWidth() {
        setMinimumWidth(dp(100));
    }

    public void disableMinimumWidth() {
        setMinimumWidth(0);
    }

    public static PinRowView leftDirectedRow(PinView pinView, String variableName) {
        return new PinRowView(pinView.getContext(), pinView, variableName, Direction.LEFT);
    }

    public static PinRowView rightDirectedRow(PinView pinView, String variableName) {
        return new PinRowView(pinView.getContext(), pinView, variableName, Direction.RIGHT);
    }

    public enum Direction {
        LEFT,
        RIGHT
    }

}
