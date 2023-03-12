package com.rodev.jmcparser;

import com.rodev.jmcparser.data.*;
import com.rodev.jbpcore.utils.StringUtils;

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
                return "pure_function";
            }

            if(data.id.startsWith("set_variable_get") && data.args.length < 3) {
                if(data.args[1].type.equals(data.origin))
                    return "variable_property";
            }

            return "function";
        });
        interpreter.setPinTypeNameHandler(StringUtils::capitalize);

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