package com.rodev.test.blueprint.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.test.blueprint.data.action.ActionRegistry;
import com.rodev.test.blueprint.data.action.type.ActionTypeRegistry;
import com.rodev.test.blueprint.data.category.ContextCategoryRegistry;
import com.rodev.test.blueprint.data.json.ActionEntity;
import com.rodev.test.blueprint.data.json.ActionTypeEntity;
import com.rodev.test.blueprint.data.json.VariableTypeEntity;
import com.rodev.test.blueprint.data.variable.VariableTypeRegistry;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DataAccess {

    public final ActionTypeRegistry actionTypeRegistry;
    public final VariableTypeRegistry variableTypeRegistry;
    public final ContextCategoryRegistry contextCategoryRegistry;
    public final ActionRegistry actionRegistry;

    private static DataAccess instance;

    private DataAccess(
            ActionTypeRegistry actionTypeRegistry,
            VariableTypeRegistry variableTypeRegistry,
            ContextCategoryRegistry contextCategoryRegistry,
            ActionRegistry actionRegistry
    ) {
        this.actionTypeRegistry = actionTypeRegistry;
        this.variableTypeRegistry = variableTypeRegistry;
        this.contextCategoryRegistry = contextCategoryRegistry;
        this.actionRegistry = actionRegistry;
    }

    public static DataAccess getInstance() {
        return instance;
    }

    @Nullable
    public static String translateCategoryId(String id) {
        var cat = getInstance().contextCategoryRegistry.get(id);

        if(cat == null) return null;

        return cat.name();
    }

    public static DataAccess load(InputStream dataInputStream) throws IOException {
        var jdo = new ObjectMapper().readValue(dataInputStream, JsonDataObject.class);

        var actionTypeRegistry = new ActionTypeRegistry();
        DataInitEventHandler.onActionTypeRegistryPreLoad(actionTypeRegistry);

        var variableTypeRegistry = new VariableTypeRegistry();
        DataInitEventHandler.onVariableTypeRegistryPreLoad(variableTypeRegistry);

        var categoryRegistry = new ContextCategoryRegistry();
        DataInitEventHandler.onContextCategoryRegistryPreLoad(categoryRegistry);

        actionTypeRegistry.load(jdo.action_types);
        variableTypeRegistry.load(jdo.variable_types);
        categoryRegistry.load(jdo.categories);

        var actionRegistry = new ActionRegistry(actionTypeRegistry, variableTypeRegistry);
        DataInitEventHandler.onActionRegistryPreLoad(actionRegistry);

        actionRegistry.load(jdo.actions);

        var dao = new DataAccess(actionTypeRegistry, variableTypeRegistry, categoryRegistry, actionRegistry);

        instance = dao;

        DataInitEventHandler.onDataAccessPostLoad(dao);

        return dao;
    }

    private static class JsonDataObject {
        public List<VariableTypeEntity> variable_types = new LinkedList<>();
        public List<ActionTypeEntity> action_types = new LinkedList<>();
        public Map<String,String> categories = new HashMap<>();
        public List<ActionEntity> actions = new LinkedList<>();
    }
}
