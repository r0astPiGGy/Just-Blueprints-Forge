package com.rodev.test.contextmenu;

import icyllis.modernui.view.View;
import icyllis.modernui.widget.TextView;

import java.util.function.Predicate;

public interface ContextMenuItem {

    String getName();

    View getView();

    void onClick();

    default boolean hideIf(Predicate<ContextMenuItem> predicate) {
        if(predicate.test(this)) {
            hide();
            return true;
        }

        return false;
    }

    boolean showIf(Predicate<ContextMenuItem> predicate);

    void show();

    void hide();

    static ContextMenuItem of(View view, String name) {
        return new ContextMenuItemImpl(view, name);
    }

    static ContextMenuItem of(String name) {
        var view = new TextView();
        view.setText(name);
        view.setTextSize(View.sp(14));

        return of(view, name);
    }

}
