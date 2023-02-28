package com.rodev.test.contextmenu;

import com.rodev.test.blueprint.data.action.Action;
import com.rodev.test.utils.TextViewCreationListener;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.TextView;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static icyllis.modernui.view.View.dp;

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

    static ContextMenuItem of(String name, String iconId, Runnable onClick) {
        var linearLayout = new LinearLayout();
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );

        var icon = new View();
        var drawable = new ImageDrawable("actions", iconId + ".png");
        icon.setBackground(drawable);
        icon.setLayoutParams(new ViewGroup.LayoutParams(dp(20), dp(20)));
        linearLayout.addView(icon);

        var view = new TextView();
        view.setText(name);
        TextViewCreationListener.onContextMenuItemTextCreated(view);
        linearLayout.addView(view);

        return of(linearLayout, name, onClick);
    }

}
