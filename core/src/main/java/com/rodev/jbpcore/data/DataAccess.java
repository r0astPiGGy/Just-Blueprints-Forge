package com.rodev.jbpcore.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.jbpcore.data.action.ActionRegistry;
import com.rodev.jbpcore.data.action.type.ActionTypeRegistry;
import com.rodev.jbpcore.data.category.ContextCategoryRegistry;
import com.rodev.jbpcore.data.icon.PinIconRegistry;
import com.rodev.jbpcore.data.json.*;
import com.rodev.jbpcore.data.selectors.SelectorGroupRegistry;
import com.rodev.jbpcore.data.variable.VariableTypeRegistry;
import com.rodev.jbpcore.handlers.DataInitEventHandler;
import com.rodev.jmcgenerator.data.GeneratorData;
import com.rodev.jmcgenerator.entity.GeneratorEntity;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class DataAccess {

    public static String TEXTURE_NAMESPACE = "justblueprints";

    public final ActionTypeRegistry actionTypeRegistry;
    public final VariableTypeRegistry variableTypeRegistry;
    public final ContextCategoryRegistry contextCategoryRegistry;
    public final ActionRegistry actionRegistry;
    public final SelectorGroupRegistry selectorGroupRegistry;
    public final GeneratorData generatorData;
    public final PinIconRegistry iconRegistry;

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

    public static Image createImage(String namespace, String name) {
        return Image.create(TEXTURE_NAMESPACE, getPath(namespace, name));
    }

    public static ImageDrawable createImageDrawable(String namespace, String name) {
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
        var iconData = objectMapper.readValue(dataProvider.getPinIconsInputStream(), PinIconEntity[].class);

        var generatorData = new GeneratorData();

        generatorData.load(generatorInputData);

        var iconRegistry = new PinIconRegistry();

        var actionTypeRegistry = new ActionTypeRegistry();
        DataInitEventHandler.onActionTypeRegistryPreLoad(actionTypeRegistry);

        var variableTypeRegistry = new VariableTypeRegistry();
        DataInitEventHandler.onVariableTypeRegistryPreLoad(variableTypeRegistry);

        var categoryRegistry = new ContextCategoryRegistry();
        DataInitEventHandler.onContextCategoryRegistryPreLoad(categoryRegistry);

        var selectorGroupRegistry = new SelectorGroupRegistry();
        DataInitEventHandler.onSelectorGroupRegistryPreLoad(selectorGroupRegistry);

        iconRegistry.load(iconData);
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
                generatorData,
                iconRegistry
        );

        instance = dao;

        DataInitEventHandler.onDataAccessPostLoad(dao);

        return dao;
    }
}
