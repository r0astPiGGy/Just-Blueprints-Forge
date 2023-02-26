package com.rodev.jmcparser;

import com.rodev.jmcparser.data.LocaleProvider;
import com.rodev.jmcparser.util.TimeCounter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Localization implements LocaleProvider {

    private final Map<String, String> values = new HashMap<>();

    public void load(InputStream inputStream) {
        values.clear();

        var counter = new TimeCounter();
        var reader = new BufferedReader(new InputStreamReader(inputStream));

        reader.lines().forEach(this::parseLine);
        counter.print(ms -> "Localization loaded in " + ms + "ms.");
    }

    private void parseLine(String line) {
        var valueByKey = line.split("=");

        if(valueByKey.length < 2) return;

        values.put(valueByKey[0], valueByKey[1]);
    }

    public String translateKey(String key) {
        var translated = values.get(key);

        if(translated == null) {
            translated = key;

            System.out.println("Localized name by key " + key + " not found. Skipping...");
        }

        return translated;
    }

}
