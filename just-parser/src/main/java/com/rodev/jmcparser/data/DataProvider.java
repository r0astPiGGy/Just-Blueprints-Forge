package com.rodev.jmcparser.data;

import com.rodev.jmcparser.Parser;
import com.sun.tools.javac.Main;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;

public class DataProvider {

    private static final String RAW_ACTIONS_URL = "https://raw.githubusercontent.com/justmc-os/jmcc/main/data/actions.json";
    private static final String RAW_LOCALE_URL = "https://gitlab.com/justmc/justmc-localization/-/raw/master/creative_plus/ru_RU.properties";
    private static final String RAW_CATEGORIES_URL = "https://raw.githubusercontent.com/justmc-os/justmc-assets/master/data/actions.json";
    private static final String RAW_EVENTS_URL = "https://raw.githubusercontent.com/justmc-os/justmc-assets/master/data/events.json";
    private static final String RAW_GAME_VALUES_URL = "https://raw.githubusercontent.com/justmc-os/justmc-assets/master/data/game_values.json";

    private static final String CUSTOM_ACTIONS_PATH = "custom_actions.json";

    public void loadActionsAndApply(Consumer<InputStream> inputStreamConsumer) {
        getInputStream(RAW_ACTIONS_URL, inputStreamConsumer);
    }

    public void loadLocaleAndApply(Consumer<InputStream> inputStreamConsumer) {
        getInputStream(RAW_LOCALE_URL, inputStreamConsumer);
    }

    public void loadCategoriesAndApply(Consumer<InputStream> inputStreamConsumer) {
        getInputStream(RAW_CATEGORIES_URL, inputStreamConsumer);
    }

    public void loadEventsAndApply(Consumer<InputStream> inputStreamConsumer) {
        getInputStream(RAW_EVENTS_URL, inputStreamConsumer);
    }

    public void loadGameValuesAndApply(Consumer<InputStream> consumer) {
        getInputStream(RAW_GAME_VALUES_URL, consumer);
    }

    public void loadCustomActionsAndApply(Consumer<InputStream> consumer) {
        getLocalInputStream(CUSTOM_ACTIONS_PATH, consumer);
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
