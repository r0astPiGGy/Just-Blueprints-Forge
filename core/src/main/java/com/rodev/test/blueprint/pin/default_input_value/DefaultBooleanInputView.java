package com.rodev.test.blueprint.pin.default_input_value;

import com.rodev.test.blueprint.data.variable.DefaultInputValue;
import icyllis.modernui.material.MaterialCheckBox;
import icyllis.modernui.view.View;

public class DefaultBooleanInputView extends MaterialCheckBox implements DefaultInputValue {

    @Override
    public String getDefaultValue() {
        return String.valueOf(isChecked());
    }

    @Override
    public View asView() {
        return this;
    }
}
