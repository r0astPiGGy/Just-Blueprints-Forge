package com.rodev.jmcparser.data;

public class EventCategoryTranslator extends CategoryTranslator {

    public EventCategoryTranslator(LocaleProvider localeProvider) {
        super(localeProvider);
    }

    protected String resolveTranslationKey(String category) {
        var localeKey = "creative_plus.category." + category;
        var translated = localeProvider.translateKey(localeKey + ".name");

        if(translated != null)
            return localeKey;

        return localeKey + "_event";
    }

    public String translateCategory(String category) {
        if(category.equals("events")) return "События";

        var localeKey = resolveTranslationKey(category) + ".name";
        var translated = localeProvider.translateKey(localeKey);

        if(translated != null) return translated;

        return localeKey;
    }

    public String translateSubCategory(String parent, String child) {
        return translateCategory(child);
    }

}
