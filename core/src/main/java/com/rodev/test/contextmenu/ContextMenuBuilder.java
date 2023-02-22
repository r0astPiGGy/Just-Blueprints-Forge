package com.rodev.test.contextmenu;

import com.rodev.test.blueprint.data.action.Action;

import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class ContextMenuBuilder {

    public final int x, y;
    protected Predicate<Action> filter = a -> false;
    protected String header;
    protected Consumer<Action> onClick = a -> {};
    protected Runnable onDismiss = () -> {};

    public ContextMenuBuilder(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public ContextMenuBuilder filtering(Predicate<Action> predicate) {
        this.filter = predicate;
        return this;
    }

    public ContextMenuBuilder withHeader(String header) {
        this.header = header;
        return this;
    }

    public ContextMenuBuilder onItemClick(Consumer<Action> onClick) {
        this.onClick = onClick;
        return this;
    }

    public ContextMenuBuilder onDismiss(Runnable onDismiss) {
        this.onDismiss = onDismiss;
        return this;
    }

    public abstract void show();

}
