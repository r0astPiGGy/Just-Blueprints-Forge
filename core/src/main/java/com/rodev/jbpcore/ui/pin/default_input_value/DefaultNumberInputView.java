package com.rodev.jbpcore.ui.pin.default_input_value;

import icyllis.modernui.core.Context;

public class DefaultNumberInputView extends DefaultTextInputView {

    public DefaultNumberInputView(Context context) {
        super(context);

        setHint("Число");
    }

    @Override
    public String getDefaultValue() {
        return getText().toString();
    }

    @Override
    public void setDefaultValue(String value) {
        setText(value);
    }

}
