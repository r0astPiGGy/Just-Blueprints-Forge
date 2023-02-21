package com.rodev.test.contextmenu;

import com.rodev.test.blueprint.data.action.Action;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.TextView;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface ContextMenuItem {

    String getName();

    View getView();

    void onClick();

    void show();

    void hide();

    static ContextMenuItem of(View view, String name, Runnable onClick) {
        return new ContextMenuItemImpl(view, name) {
            @Override
            public void onClick() {
                onClick.run();
            }
        };
    }

    static ContextMenuItem of(String name, Runnable onClick) {
        var view = new TextView();
        view.setText(name);
        view.setTextSize(View.sp(14));

        return of(view, name, onClick);
    }

}
