package com.rodev.jmcparser;

import com.rodev.jmcparser.data.DataInterpreter;
import com.rodev.jmcparser.data.DataParser;
import com.rodev.jmcparser.data.DataProvider;
import com.rodev.jmcparser.data.JsonDataWriter;

import java.io.File;
import java.io.IOException;

public class Parser implements Runnable {

    private final Localization localization = new Localization();
    private final DataProvider dataProvider = new DataProvider();

    private final DataParser dataParser = new DataParser();
    private final ActionCategories categories = new ActionCategories();
    private final JsonDataWriter actionWriter = new JsonDataWriter(new File("actions.json"));

    @Override
    public void run() {
        dataProvider.loadLocaleAndApply(localization::load);
        dataProvider.loadActionsAndApply(dataParser::load);
        dataProvider.loadCategoriesAndApply(categories::load);

        DataInterpreter interpreter = dataParser.createInterpreter(localization, categories);

        var actions = interpreter.interpret();

        actionWriter.write(actions);
    }

    public static void main(String[] args) {
        new Parser().run();
    }
}