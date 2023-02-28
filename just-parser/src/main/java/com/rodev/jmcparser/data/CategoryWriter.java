package com.rodev.jmcparser.data;

import com.rodev.test.blueprint.data.json.ActionEntity;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CategoryWriter {

    private final JsonDataWriter jsonDataWriter;
    private final LocaleProvider localeProvider;

    public CategoryWriter(File fileToWrite, LocaleProvider localeProvider) {
        this.jsonDataWriter = new JsonDataWriter(fileToWrite);
        this.localeProvider = localeProvider;
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


        if(split.length > 1) {
            String subCategoryKey = split[1];

            var translatedSubCategory = translateSubCategory(categoryKey, subCategoryKey.split("-")[1]);

            map.put(subCategoryKey, translatedSubCategory);
        }

        var translatedCategory = translateCategory(categoryKey);

        map.put(categoryKey, translatedCategory);
    }

    private String resolveTranslationKey(String category) {
        var localeKey = "creative_plus.category." + category;
        var translated = localeProvider.translateKey(localeKey + ".name");

        if(translated != null)
            return localeKey;

        return localeKey + "_action";
    }

    private String translateCategory(String category) {
        var localeKey = resolveTranslationKey(category) + ".name";
        var translated = localeProvider.translateKey(localeKey);

        if(translated != null) return translated;

        return localeKey;
    }

    private String translateSubCategory(String parent, String child) {
        var parentKey = resolveTranslationKey(parent);
        var localeKey = String.format("%s.subcategory.%s.name", parentKey, child);
        var translated = localeProvider.translateKey(localeKey);

        if(translated != null) return translated;

        return localeKey;
    }


}
