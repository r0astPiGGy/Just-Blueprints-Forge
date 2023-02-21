package com.rodev.test.blueprint.data.category;

import org.jetbrains.annotations.Nullable;

public abstract class Category {

    public final String name;
    public final String id;

    public Category(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Nullable
    public abstract Category getCategory(String id);
}
