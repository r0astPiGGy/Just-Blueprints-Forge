package com.rodev.jmcparser.data;

import com.rodev.jmcparser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;
import java.util.function.Function;

public class DataProvider {

    private static final String RAW_ACTIONS_URL = "https://raw.githubusercontent.com/justmc-os/jmcc/main/data/actions.json";
    private static final String RAW_LOCALE_URL = "https://gitlab.com/justmc/justmc-localization/-/raw/master/creative_plus/ru_RU.properties";
    private static final String RAW_ALTERNATE_ACTIONS = "https://raw.githubusercontent.com/justmc-os/justmc-assets/master/data/actions.json";
    private static final String RAW_EVENTS_URL = "https://raw.githubusercontent.com/justmc-os/justmc-assets/master/data/events.json";
    private static final String RAW_GAME_VALUES_URL = "https://raw.githubusercontent.com/justmc-os/justmc-assets/master/data/game_values.json";
    private static final String RAW_GAME_VALUES_MAPPING = "https://raw.githubusercontent.com/justmc-os/jmcc/main/data/values.json";

    private static final String CUSTOM_ACTIONS_PATH = "custom_actions.json";
    private static final String ACTION_PATCHES_PATH = "patches/actions.json";
    private static final String CATEGORY_PATCHES_PATH = "patches/categories.json";
    private static final String ACTION_TYPE_PATCHES_PATH = "patches/action_types.json";
    private static final String VARIABLE_TYPE_PATCHES_PATH = "patches/variable_types.json";
    private static final String GENERATOR_DATA_PATCHES_PATH = "patches/generator_data.json";
    private static final String PIN_ICONS_PATH = "pin_icons.json";

    public void loadActionsAndApply(Consumer<InputStream> inputStreamConsumer) {
        getInputStream(RAW_ACTIONS_URL, inputStreamConsumer);
    }

    public void loadLocaleAndApply(Consumer<InputStream> inputStreamConsumer) {
        getInputStream(RAW_LOCALE_URL, inputStreamConsumer);
    }

    public void loadAlternateActions(Consumer<InputStream> inputStreamConsumer) {
        getInputStream(RAW_ALTERNATE_ACTIONS, inputStreamConsumer);
    }

    public void loadEventsAndApply(Consumer<InputStream> inputStreamConsumer) {
        getInputStream(RAW_EVENTS_URL, inputStreamConsumer);
    }

    public void loadGameValuesAndApply(Consumer<InputStream> consumer) {
        getInputStream(RAW_GAME_VALUES_URL, consumer);
    }

    public void loadMappedGameValuesAndApply(Consumer<InputStream> consumer) {
        getInputStream(RAW_GAME_VALUES_MAPPING, consumer);
    }

    public void loadCustomActionsAndApply(Consumer<InputStream> consumer) {
        getLocalInputStream(CUSTOM_ACTIONS_PATH, consumer);
    }

    public void loadActionPatchesAndApply(Consumer<InputStream> consumer) {
        getLocalInputStream(ACTION_PATCHES_PATH, consumer);
    }

    public void loadVariableTypePatchesAndApply(Consumer<InputStream> consumer) {
        getLocalInputStream(VARIABLE_TYPE_PATCHES_PATH, consumer);
    }

    public void loadGeneratorDataPatchesAndApply(Consumer<InputStream> consumer) {
        getLocalInputStream(GENERATOR_DATA_PATCHES_PATH, consumer);
    }

    public void loadCategoryPatchesAndApply(Consumer<InputStream> consumer) {
        getLocalInputStream(CATEGORY_PATCHES_PATH, consumer);
    }

    public void loadActionTypePatchesAndApply(Consumer<InputStream> consumer) {
        getLocalInputStream(ACTION_TYPE_PATCHES_PATH, consumer);
    }

    public <T> T loadCustomActionsAndReturn(Function<InputStream, T> function) {
        return getLocalInputStream(CUSTOM_ACTIONS_PATH, function);
    }

    public <T> T loadPinIconsAndReturn(Function<InputStream, T> function) {
        return getLocalInputStream(PIN_ICONS_PATH, function);
    }

    private <T> T getLocalInputStream(String resId, Function<InputStream, T> function) {
        try (var is = Parser.class.getResourceAsStream(resId)) {
            return function.apply(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getLocalInputStream(String resId, Consumer<InputStream> inputStreamConsumer) {
        try (var is = Parser.class.getResourceAsStream(resId)) {
            inputStreamConsumer.accept(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getInputStream(String rawUrl, Consumer<InputStream> inputStreamConsumer) {
        URLConnection con;
        try {
            var url = new URL(rawUrl);
            con = url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try(var is = con.getInputStream()) {
            inputStreamConsumer.accept(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
