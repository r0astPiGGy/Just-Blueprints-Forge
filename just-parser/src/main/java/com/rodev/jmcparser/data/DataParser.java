package com.rodev.jmcparser.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.jmcparser.Localization;
import com.rodev.jmcparser.json.ActionData;
import com.rodev.jmcparser.util.TimeCounter;

import java.io.IOException;
import java.io.InputStream;

public class DataParser {

    private ActionData[] actionData;

    public void load(InputStream inputStream) {
        try {
            var counter = new TimeCounter();
            actionData = new ObjectMapper().readValue(inputStream, ActionData[].class);
            counter.print(ms -> "Loaded " + actionData.length + " actions in " + ms + "ms.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public DataInterpreter createInterpreter(LocaleProvider localeProvider, CategoryProvider categoryProvider) {
        return new DataInterpreter(actionData, localeProvider, categoryProvider);
    }

}
