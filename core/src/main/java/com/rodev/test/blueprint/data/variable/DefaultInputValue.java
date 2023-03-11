package com.rodev.test.blueprint.data.variable;

import icyllis.modernui.view.View;

public interface DefaultInputValue {

    String getDefaultValue();

    void setDefaultValue(String value);

    View asView();

    default void show() {
        asView().setVisibility(View.VISIBLE);
    }

    default void hide() {
        asView().setVisibility(View.GONE);
    }

}
