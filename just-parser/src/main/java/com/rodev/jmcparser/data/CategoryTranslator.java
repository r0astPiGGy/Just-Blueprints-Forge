package com.rodev.jmcparser.data;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryTranslator {

    protected final LocaleProvider localeProvider;

    protected String resolveTranslationKey(String category) {
        var localeKey = "creative_plus.category." + category;
        var translated = localeProvider.translateKey(localeKey + ".name");

        if(translated != null)
            return localeKey;

        return localeKey + "_action";
    }

    public String translateCategory(String category) {
        var localeKey = resolveTranslationKey(category) + ".name";
        var translated = localeProvider.translateKey(localeKey);

        if(translated != null) return translated;

        return localeKey;
    }

    public String translateSubCategory(String parent, String child) {
        var parentKey = resolveTranslationKey(parent);
        var localeKey = String.format("%s.subcategory.%s.name", parentKey, child);
        var translated = localeProvider.translateKey(localeKey);

        if(translated != null) return translated;

        return localeKey;
    }

}
