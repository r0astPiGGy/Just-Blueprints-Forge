package com.rodev.test.blueprint.data;

import icyllis.modernui.graphics.drawable.ImageDrawable;

public interface IconSupplier {

    IconSupplier actionIconSupplier = id -> new ImageDrawable("actions", id + ".png");

    IconSupplier gameValueIconSupplier = id -> {
        id = id.replace("_gamevalue_getter", "");
        return new ImageDrawable("game_values", id + ".png");
    };

    ImageDrawable create(String id);

}
