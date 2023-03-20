package com.rodev.jbpcore.blueprint.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.jbpcore.blueprint.data.action.ActionRegistry;
import com.rodev.jbpcore.blueprint.data.action.type.ActionTypeRegistry;
import com.rodev.jbpcore.blueprint.data.category.ContextCategoryRegistry;
import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jbpcore.blueprint.data.json.ActionTypeEntity;
import com.rodev.jbpcore.blueprint.data.json.SelectorGroupEntity;
import com.rodev.jbpcore.blueprint.data.json.VariableTypeEntity;
import com.rodev.jbpcore.blueprint.data.selectors.SelectorGroupRegistry;
import com.rodev.jbpcore.blueprint.data.variable.VariableTypeRegistry;
import com.rodev.jmcgenerator.data.GeneratorData;
import com.rodev.jmcgenerator.entity.GeneratorEntity;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class DataAccess {

    public static String TEXTURE_NAMESPACE = "justblueprints";

    public final ActionTypeRegistry actionTypeRegistry;
    public final VariableTypeRegistry variableTypeRegistry;
    public final ContextCategoryRegistry contextCategoryRegistry;
    public final ActionRegistry actionRegistry;
    public final SelectorGroupRegistry selectorGroupRegistry;
    public final GeneratorData generatorData;

    private static DataAccess instance;

    public static DataAccess getInstance() {
        return instance;
    }

    @Nullable
    public static String translateCategoryId(String id) {
        var cat = getInstance().contextCategoryRegistry.get(id);

        if(cat == null) return null;

        return cat.name();
    }

    public static String getPath(String namespace, String name) {
        return String.format("%s/%s.png", namespace, name);
    }

    public static ImageDrawable createImage(String namespace, String name) {
        return new ImageDrawable(TEXTURE_NAMESPACE, getPath(namespace, name));
    }

    public static DataAccess load(DataProvider dataProvider) throws IOException {
        DataInitEventHandler.onDataPreLoad();

        var objectMapper = new ObjectMapper();

        var actions = objectMapper.readValue(dataProvider.getActionsInputStream(), ActionEntity[].class);
        var categories = objectMapper.readValue(dataProvider.getCategoriesInputStream(), Map.class);
        var variable_types = objectMapper.readValue(dataProvider.getVariableTypesInputStream(), VariableTypeEntity[].class);
        var action_types = objectMapper.readValue(dataProvider.getActionTypesInputStream(), ActionTypeEntity[].class);
        var selectorGroups = objectMapper.readValue(dataProvider.getSelectorGroupsInputStream(), SelectorGroupEntity[].class);
        var generatorInputData = objectMapper.readValue(dataProvider.getDataGeneratorInputStream(), GeneratorEntity[].class);

        var generatorData = new GeneratorData();

        generatorData.load(generatorInputData);

        var actionTypeRegistry = new ActionTypeRegistry();
        DataInitEventHandler.onActionTypeRegistryPreLoad(actionTypeRegistry);

        var variableTypeRegistry = new VariableTypeRegistry();
        DataInitEventHandler.onVariableTypeRegistryPreLoad(variableTypeRegistry);

        var categoryRegistry = new ContextCategoryRegistry();
        DataInitEventHandler.onContextCategoryRegistryPreLoad(categoryRegistry);

        var selectorGroupRegistry = new SelectorGroupRegistry();
        DataInitEventHandler.onSelectorGroupRegistryPreLoad(selectorGroupRegistry);

        actionTypeRegistry.load(action_types);
        variableTypeRegistry.load(variable_types);
        selectorGroupRegistry.load(selectorGroups);

        //noinspection unchecked
        categoryRegistry.load(categories);

        var actionRegistry = new ActionRegistry(actionTypeRegistry, variableTypeRegistry);
        DataInitEventHandler.onActionRegistryPreLoad(actionRegistry);

        actionRegistry.load(actions);

        var dao = new DataAccess(
                actionTypeRegistry,
                variableTypeRegistry,
                categoryRegistry,
                actionRegistry,
                selectorGroupRegistry,
                generatorData
        );

        instance = dao;

        DataInitEventHandler.onDataAccessPostLoad(dao);

        return dao;
    }
}
