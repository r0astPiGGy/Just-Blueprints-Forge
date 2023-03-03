package com.rodev.test.blueprint.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.test.blueprint.data.action.ActionRegistry;
import com.rodev.test.blueprint.data.action.type.ActionTypeRegistry;
import com.rodev.test.blueprint.data.category.ContextCategoryRegistry;
import com.rodev.test.blueprint.data.json.ActionEntity;
import com.rodev.test.blueprint.data.json.ActionTypeEntity;
import com.rodev.test.blueprint.data.json.SelectorGroupEntity;
import com.rodev.test.blueprint.data.json.VariableTypeEntity;
import com.rodev.test.blueprint.data.selectors.SelectorGroupRegistry;
import com.rodev.test.blueprint.data.variable.VariableTypeRegistry;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RequiredArgsConstructor
public class DataAccess {

    public final ActionTypeRegistry actionTypeRegistry;
    public final VariableTypeRegistry variableTypeRegistry;
    public final ContextCategoryRegistry contextCategoryRegistry;
    public final ActionRegistry actionRegistry;
    public final SelectorGroupRegistry selectorGroupRegistry;

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

    public static DataAccess load(DataProvider dataProvider) throws IOException {
        var objectMapper = new ObjectMapper();

        var actions = objectMapper.readValue(dataProvider.getActionsInputStream(), ActionEntity[].class);
        var categories = objectMapper.readValue(dataProvider.getCategoriesInputStream(), Map.class);
        var variable_types = objectMapper.readValue(dataProvider.getVariableTypesInputStream(), VariableTypeEntity[].class);
        var action_types = objectMapper.readValue(dataProvider.getActionTypesInputStream(), ActionTypeEntity[].class);
        var selectorGroups = objectMapper.readValue(dataProvider.getSelectorGroupsInputStream(), SelectorGroupEntity[].class);

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

        var dao = new DataAccess(actionTypeRegistry, variableTypeRegistry, categoryRegistry, actionRegistry, selectorGroupRegistry);

        instance = dao;

        DataInitEventHandler.onDataAccessPostLoad(dao);

        return dao;
    }
}
