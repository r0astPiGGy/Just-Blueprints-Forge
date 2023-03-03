package com.rodev.jmcparser;

import com.rodev.jmcparser.data.*;

import java.io.File;

public class Parser implements Runnable {

    private final Localization localization = new Localization();
    private final DataProvider dataProvider = new DataProvider();

    private final DataParser dataParser = new DataParser();
    private final ActionCategories categories = new ActionCategories();
    private final JsonDataWriter actionWriter = new JsonDataWriter(parserDirectoryChild("actions.json"));

    @Override
    public void run() {
        dataParser.parseUsing(dataProvider);
        dataProvider.loadLocaleAndApply(localization::load);
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

        var categoryWriter = new CategoryWriter(parserDirectoryChild("categories.json"), localization);
        var variableTypeWriter = new VariableTypeWriter(parserDirectoryChild("variable_types.json"));

        actionWriter.write(actions);
        categoryWriter.write(actions);
        variableTypeWriter.write(actions);
    }

    public static File parserDirectoryChild(String fileName) {
        File file = new File("parser-output");
        //noinspection ResultOfMethodCallIgnored
        file.mkdir();

        return new File(file, fileName);
    }

    public static void main(String[] args) {
        new Parser().run();
    }
}