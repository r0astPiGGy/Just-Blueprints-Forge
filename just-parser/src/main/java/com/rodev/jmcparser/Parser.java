package com.rodev.jmcparser;

import com.rodev.jmcparser.data.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

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

        interpreter.setActionTypeHandler(data -> {
            var containing = data.containing;

            if(containing != null && containing.equalsIgnoreCase("predicate")) {
                return "pure-function";
            }

            return "function";
        });

        var actions = interpreter.interpret();

        var categoryWriter = new CategoryWriter(new File("categories.json"), localization);
        var variableTypeWriter = new VariableTypeWriter(new File("variable_types.json"));

        actionWriter.write(actions);
        categoryWriter.write(actions);
        variableTypeWriter.write(actions);
    }

    public static void main(String[] args) {
        new Parser().run();
    }
}