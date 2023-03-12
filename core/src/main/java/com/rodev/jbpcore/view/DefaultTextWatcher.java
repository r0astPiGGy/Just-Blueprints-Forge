package com.rodev.jbpcore.view;

import icyllis.modernui.text.Editable;
import icyllis.modernui.text.TextWatcher;

public interface DefaultTextWatcher extends TextWatcher {

    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    default void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    default void afterTextChanged(Editable s) {}

}
