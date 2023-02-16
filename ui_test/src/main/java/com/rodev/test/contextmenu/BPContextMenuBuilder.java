package com.rodev.test.contextmenu;

import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.view.ContextMenu;
import icyllis.modernui.view.View;
import icyllis.modernui.view.menu.MenuBuilder;
import icyllis.modernui.view.menu.MenuPopupHelper;

import javax.annotation.Nonnull;

public class BPContextMenuBuilder extends MenuBuilder implements ContextMenu {

    public BPContextMenuBuilder() {
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

        if (getVisibleItems().size() > 0) {
            int[] location = new int[2];
            assert originalView != null;
            originalView.getLocationInWindow(location);

            final BPMenuPopupHelper helper = new BPMenuPopupHelper(this, originalView, false);
            helper.show(Math.round(x), Math.round(y));
            return helper;
        }

        return null;
    }
}
