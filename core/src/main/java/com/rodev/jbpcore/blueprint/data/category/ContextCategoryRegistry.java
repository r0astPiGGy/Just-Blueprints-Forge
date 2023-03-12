package com.rodev.jbpcore.blueprint.data.category;

import com.rodev.jbpcore.blueprint.data.Registry;

import java.util.Map;

public class ContextCategoryRegistry extends Registry<String, Category> {

    public void load(Map<String, String> rawCategoryMap) {
        data.clear();
        rawCategoryMap.forEach((id, name) -> {
            data.put(id, new Category(name, id));
        });
    }

}
