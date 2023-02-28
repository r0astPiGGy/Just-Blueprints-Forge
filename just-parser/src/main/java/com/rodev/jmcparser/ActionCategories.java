package com.rodev.jmcparser;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.jmcparser.data.CategoryProvider;
import com.rodev.jmcparser.util.TimeCounter;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ActionCategories implements CategoryProvider {

    private final Map<String, String> categoriesById = new HashMap<>();

    public void load(InputStream inputStream) {
        var counter = new TimeCounter();

        var objMapper = new ObjectMapper();

        ActionEntity[] data;

        try {
            data = objMapper.readValue(inputStream, ActionEntity[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (var entity : data) {
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

    private static class ActionEntity {
        public String id;
        public String category;
        @Nullable
        public String subcategory;

        @JsonIgnore
        public Object type;

        @JsonIgnore
        public Object args;

        @JsonIgnore
        public Object icon;

    }
}
