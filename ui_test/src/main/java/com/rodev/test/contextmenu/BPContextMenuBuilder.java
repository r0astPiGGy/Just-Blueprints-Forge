package com.rodev.test.contextmenu;

import com.rodev.test.blueprint.data.action.Action;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.view.ContextMenu;
import icyllis.modernui.view.View;
import icyllis.modernui.view.menu.MenuBuilder;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class BPContextMenuBuilder extends MenuBuilder implements ContextMenu {

    private final Consumer<Action> onItemClick;

    public BPContextMenuBuilder(Consumer<Action> onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Nonnull
    @Override
    public ContextMenu setHeaderIcon(Drawable icon) {
        super.setHeaderIconInt(icon);
        return this;
    }

    @Nonnull
    @Override
    public ContextMenu setHeaderTitle(CharSequence title) {
        super.setHeaderTitleInt(title);
        return this;
    }

    @Nonnull
    @Override
    public ContextMenu setHeaderView(View view) {
        super.setHeaderViewInt(view);
        return this;
    }

    public BPMenuPopupHelper showPopup(View originalView, float x, float y) {
        if (originalView != null) {
            // Let relevant views and their populate context listeners populate
            // the context menu
            originalView.createContextMenu(this);
        }

        int[] location = new int[2];
        assert originalView != null;
        originalView.getLocationInWindow(location);

        final BPMenuPopupHelper helper = new BPMenuPopupHelper(onItemClick, originalView);
        helper.show(Math.round(x), Math.round(y));

        return helper;
    }
}
