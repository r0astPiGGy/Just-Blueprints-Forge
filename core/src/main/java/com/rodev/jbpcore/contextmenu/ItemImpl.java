package com.rodev.jbpcore.contextmenu;

import icyllis.modernui.view.View;

public abstract class ItemImpl implements Item {

    private final View root;
    private final String name;

    public ItemImpl(View root, String name) {
        this.root = root;
        this.name = name;

        root.setOnClickListener(view -> onClick());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public View getView() {
        return root;
    }

    @Override
    public void show() {
        if (root.getVisibility() == View.GONE) {
            root.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hide() {
        if (root.getVisibility() == View.VISIBLE) {
            root.setVisibility(View.GONE);
        }
    }
}
