package com.rodev.test.blueprint.data.category;

import com.rodev.test.blueprint.data.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ContextCategoryRegistry extends Registry<String, Category> {

    public void load(Map<String, String> rawCategoryMap) {
        data.clear();
        rawCategoryMap.forEach((id, name) -> {
            data.put(id, new Category(name, id));
        });
    }

}
