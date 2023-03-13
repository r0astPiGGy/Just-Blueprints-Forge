package com.rodev.jmcparser.data.category;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jmcparser.data.DataWriter;
import com.rodev.jmcparser.data.JsonDataWriter;
import com.rodev.jmcparser.data.LocaleProvider;
import com.rodev.jmcparser.data.event.EventCategoryTranslator;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CategoryWriter extends DataWriter<ActionEntity, Category> {

    private final CategoryTranslator categoryTranslator;
    private final EventCategoryTranslator eventCategoryTranslator;

    public CategoryWriter(File fileToWrite, LocaleProvider localeProvider) {
        super(fileToWrite);
        this.categoryTranslator = new CategoryTranslator(localeProvider);
        this.eventCategoryTranslator = new EventCategoryTranslator(localeProvider);
    }

    public void write(ActionEntity[] actions) {
        var map = Arrays.stream(actions)
                .map(a -> a.category)
                .distinct()
                .collect(HashMap::new, this::addCategory, HashMap::putAll);

        writeToFile(map);
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

        var patched = patch(new Category(categoryKey, translatedCategory));
        if(patched == null) return;

        map.put(patched.key, patched.name);
    }

}
