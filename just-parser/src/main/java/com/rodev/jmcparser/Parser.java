package com.rodev.jmcparser;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jbpcore.blueprint.data.json.VariableTypeEntity;
import com.rodev.jmcparser.data.*;
import com.rodev.jmcparser.data.action.ActionWriter;
import com.rodev.jmcparser.data.category.Category;
import com.rodev.jmcparser.data.category.CategoryWriter;
import com.rodev.jmcparser.patcher.AbstractPatcher;
import com.rodev.jmcparser.patcher.Patcher;
import com.rodev.jmcparser.patcher.action.ActionEntityPatch;
import com.rodev.jmcparser.patcher.action.ActionPatcher;
import com.rodev.jmcparser.patcher.category.CategoryPatch;
import com.rodev.jmcparser.patcher.variable.VariableTypePatch;

import java.io.File;
import java.util.function.Function;
import static com.rodev.jmcparser.data.Parser.parseJson;

public class Parser implements Runnable {

    private final Localization localization = new Localization();
    private final DataProvider dataProvider = new DataProvider();

    private final DataParser dataParser = new DataParser();
    private final ActionCategories categories = new ActionCategories();

    @Override
    public void run() {
        var helper = new ParserRegisterHelper(localization, categories, dataParser);

        dataProvider.loadLocaleAndApply(localization::load);
        dataProvider.loadCategoriesAndApply(categories::load);

        helper.registerParsers();

        dataParser.parseUsing(dataProvider);

        DataInterpreter interpreter = dataParser.createInterpreter();

        var actions = interpreter.interpret();

        var actionWriter = new ActionWriter(parserDirectoryChild("actions.json"));
        dataProvider.loadActionPatchesAndApply(is -> {
            var patches = parseJson(is, ActionEntityPatch[].class);
            var patcher = new ActionPatcher(patches);

            actionWriter.setPatcher(patcher);
        });
        var categoryWriter = new CategoryWriter(parserDirectoryChild("categories.json"), localization);
        dataProvider.loadCategoryPatchesAndApply(is -> {
            var patches = parseJson(is, CategoryPatch[].class);
            Patcher<Category> patcher = AbstractPatcher.defaultPatcher(patches, c -> c.key);

            categoryWriter.setPatcher(patcher);
        });
        var variableTypeWriter = new VariableTypeWriter(parserDirectoryChild("variable_types.json"));
        dataProvider.loadVariableTypePatchesAndApply(is -> {
            var patches = parseJson(is, VariableTypePatch[].class);
            Patcher<VariableTypeEntity> patcher = AbstractPatcher.defaultPatcher(patches, t -> t.id);

            variableTypeWriter.setPatcher(patcher);
        });

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