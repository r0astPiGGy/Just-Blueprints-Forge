package com.rodev.jbpcore.ui.pin.default_input_value;

public class DefaultNumberInputView extends DefaultTextInputView {

    public DefaultNumberInputView() {
        super();

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
