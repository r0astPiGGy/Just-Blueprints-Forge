package com.rodev.jmcparser.data;

import com.rodev.test.blueprint.data.json.ActionEntity;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CategoryWriter {

    private final JsonDataWriter jsonDataWriter;

    private final CategoryTranslator categoryTranslator;
    private final EventCategoryTranslator eventCategoryTranslator;

    public CategoryWriter(File fileToWrite, LocaleProvider localeProvider) {
        this.jsonDataWriter = new JsonDataWriter(fileToWrite);
        this.categoryTranslator = new CategoryTranslator(localeProvider);
        this.eventCategoryTranslator = new EventCategoryTranslator(localeProvider);
    }

    public void write(ActionEntity[] actions) {
        var map = Arrays.stream(actions)
                .map(a -> a.category)
                .distinct()
                .collect(HashMap::new, this::addCategory, HashMap::putAll);

        jsonDataWriter.write(map);
    }

    private void addCategory(Map<Object, Object> map, String category) {
        var split = category.split("\\.");
        var categoryKey = split[0];

        var categoryTranslator = this.categoryTranslator;

        if(categoryKey.equals("events")) {
            categoryTranslator = eventCategoryTranslator;
        }

        if(split.length > 1) {
            String subCategoryKey = split[1];

            var translatedSubCategory = categoryTranslator.translateSubCategory(categoryKey, subCategoryKey.split("-")[1]);
            map.put(subCategoryKey, translatedSubCategory);
        }

        var translatedCategory = categoryTranslator.translateCategory(categoryKey);
        map.put(categoryKey, translatedCategory);
    }

}
