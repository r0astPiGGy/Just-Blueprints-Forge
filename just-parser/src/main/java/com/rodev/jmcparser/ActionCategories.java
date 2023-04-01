package com.rodev.jmcparser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.jmcparser.data.action.alternate.AlternateActionProvider;
import com.rodev.jmcparser.data.category.CategoryProvider;
import com.rodev.jmcparser.util.TimeCounter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ActionCategories implements CategoryProvider {

    private final Map<String, String> categoriesById = new HashMap<>();

    public void load(AlternateActionProvider alternateActionProvider) {
        var counter = new TimeCounter();

        for (var entity : alternateActionProvider.getAll()) {
            if(entity == null) continue;

            var category = entity.category;
            var sub = entity.subcategory;

            if(sub != null) {
                category = String.format("%s.%s-%s", category, category, sub);
            }

            categoriesById.put(entity.id, category);
        }

        counter.print(estimated -> "Loaded " + categoriesById.size() + " actions by categories in " + estimated + "ms.");
    }

    @Override
    public String getCategoryForActionId(String id) {
        return categoriesById.get(id);
    }

}
