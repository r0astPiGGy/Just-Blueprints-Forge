package com.rodev.jmcparser;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jbpcore.blueprint.data.json.ActionTypeEntity;
import com.rodev.jbpcore.blueprint.data.json.PinIconEntity;
import com.rodev.jbpcore.blueprint.data.json.VariableTypeEntity;
import com.rodev.jmcgenerator.entity.GeneratorEntity;
import com.rodev.jmcparser.data.*;
import com.rodev.jmcparser.data.category.Category;
import com.rodev.jmcparser.data.category.CategoryWriter;
import com.rodev.jmcparser.patcher.AbstractPatcher;
import com.rodev.jmcparser.patcher.Patcher;
import com.rodev.jmcparser.patcher.action.ActionEntityPatch;
import com.rodev.jmcparser.patcher.action.ActionPatcher;
import com.rodev.jmcparser.patcher.action.ActionTypeEntityPatch;
import com.rodev.jmcparser.patcher.category.CategoryPatch;
import com.rodev.jmcparser.patcher.generator.GeneratorEntityPatch;
import com.rodev.jmcparser.patcher.variable.VariableTypePatch;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static com.rodev.jmcparser.data.Parser.parseJson;

public class Parser implements Runnable {

    private final Localization localization = new Localization();
    private final DataProvider dataProvider = new DataProvider();

    private final DataParser dataParser = new DataParser();
    private final ActionCategories categories = new ActionCategories();

    @Override
    public void run() {
        var helper = new ParserRegisterHelper(localization, categories, dataParser);

        helper.onDataProvide(dataProvider);

        dataProvider.loadLocaleAndApply(localization::load);
        dataProvider.loadCategoriesAndApply(categories::load);

        helper.registerParsers();

        dataParser.parseUsing(dataProvider);

        DataInterpreter interpreter = dataParser.createInterpreter();

        var actions = interpreter.interpret();


        DataWriter.Default<ActionEntity> actionWriter
                = DataWriter.defaultDataWriter(parserDirectoryChild("actions.json"));
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


        DataWriter.Default<GeneratorEntity> generatorDataWriter
                = DataWriter.defaultDataWriter(parserDirectoryChild("generator_data.json"));
        dataProvider.loadGeneratorDataPatchesAndApply(is -> {
            var patches = parseJson(is, GeneratorEntityPatch[].class);
            Patcher<GeneratorEntity> patcher = AbstractPatcher.defaultPatcher(patches, t -> t.id);

            generatorDataWriter.setPatcher(patcher);
        });


        var actionTypeWriter = new ActionTypeWriter(parserDirectoryChild("action_types.json"));
        dataProvider.loadActionTypePatchesAndApply(is -> {
            var patches = parseJson(is, ActionTypeEntityPatch[].class);
            Patcher<ActionTypeEntity> patcher = AbstractPatcher.defaultPatcher(patches, t -> t.id);

            actionTypeWriter.setPatcher(patcher);
        });

        var pinIconWriter = DataWriter.defaultDataWriter(parserDirectoryChild("pin_icons.json"));
        var icons = dataProvider.loadPinIconsAndReturn(is -> parseJson(is, PinIconEntity[].class));

        var generatorDataEntities = helper.getDataGenerator().getGeneratorEntities();

        applyAdditionalDirectories(
                actionWriter,
                categoryWriter,
                variableTypeWriter,
                generatorDataWriter,
                actionTypeWriter,
                pinIconWriter
        );

        actionWriter.write(actions);
        categoryWriter.write(actions);
        variableTypeWriter.write(actions);
        actionTypeWriter.write(actions);
        pinIconWriter.write(icons);
        generatorDataWriter.write(generatorDataEntities);

        helper.onAllDataInterpreted();
    }

    private void applyAdditionalDirectories(DataWriter<?, ?>... writers) {
        for (DataWriter<?, ?> writer : writers) {
            writer.setAdditionalDestinationDirectories(
                    new File("ui_testing/src/main/resources/com/rodev/jbpcore/"),
                    new File("src/main/resources/com/rodev/jbp/")
            );
        }
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