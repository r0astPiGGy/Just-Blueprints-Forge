package com.rodev.jbpcore.blueprint.pin.default_input_value;

import com.rodev.jbpcore.blueprint.data.variable.DefaultInputValue;
import icyllis.modernui.material.MaterialCheckBox;
import icyllis.modernui.view.View;

public class DefaultBooleanInputView extends MaterialCheckBox implements DefaultInputValue {

    @Override
    public String getDefaultValue() {
        return "\"" + String.valueOf(isChecked()).toUpperCase() + "\"";
    }

    @Override
    public void setDefaultValue(String value) {
        value = value.replace("\"", "");
        setChecked(Boolean.parseBoolean(value));
    }

    @Override
    public View asView() {
        return this;
    }
}
