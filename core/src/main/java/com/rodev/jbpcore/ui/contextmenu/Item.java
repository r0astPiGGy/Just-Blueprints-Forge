package com.rodev.jbpcore.ui.contextmenu;

import com.rodev.jbpcore.handlers.TextViewCreationListener;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.TextView;

public interface Item {

    String getName();

    View getView();

    void onClick();

    void show();

    void hide();

    static Item of(View view, String name, Runnable onClick) {
        return new ItemImpl(view, name) {
            @Override
            public void onClick() {
                onClick.run();
            }
        };
    }

    static Item of(Context context, String name, ImageDrawable imageDrawable, Runnable onClick) {
        var linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        );

        var icon = new View(context);
        icon.setBackground(imageDrawable);
        icon.setLayoutParams(new ViewGroup.LayoutParams(icon.dp(20), icon.dp(20)));
        linearLayout.addView(icon);

        var view = new TextView(context);
        view.setText(name);
        TextViewCreationListener.onContextMenuItemTextCreated(view);
        linearLayout.addView(view);

        return of(linearLayout, name, onClick);
    }

}
