package com.rodev.jbpcore.blueprint.data;

import com.rodev.jbpcore.blueprint.data.action.Action;
import com.rodev.jbpcore.blueprint.data.action.ActionRegistry;
import com.rodev.jbpcore.blueprint.data.action.EnumPinType;
import com.rodev.jbpcore.blueprint.data.action.type.ActionTypeRegistry;
import com.rodev.jbpcore.blueprint.data.category.ContextCategoryRegistry;
import com.rodev.jbpcore.blueprint.data.selectors.SelectorGroup;
import com.rodev.jbpcore.blueprint.data.selectors.SelectorGroupRegistry;
import com.rodev.jbpcore.blueprint.data.variable.VariableTypeRegistry;
import com.rodev.jbpcore.blueprint.node.BPNode;
import com.rodev.jbpcore.blueprint.node.impl.NodeView;
import com.rodev.jbpcore.blueprint.node.impl.getter.PropertyGetterNode;
import com.rodev.jbpcore.blueprint.node.impl.getter.PureGetterNode;
import com.rodev.jbpcore.blueprint.node.impl.getter.SelectorVariableGetterNode;
import com.rodev.jbpcore.blueprint.pin.default_input_value.*;
import icyllis.modernui.graphics.opengl.TextureManager;

import java.util.Map;

import static com.rodev.jbpcore.blueprint.data.DataAccess.TEXTURE_NAMESPACE;

public class DataInitEventHandler {

    private DataInitEventHandler() {}

    public static void onDataPreLoad() {
        IconPathResolver.registerResolver("game_values", action -> {
            return String.format("%s/%s.png", action.iconNamespace(), action.id().replace("_gamevalue_getter", ""));
        });
    }

    public static void onActionTypeRegistryPreLoad(ActionTypeRegistry registry) {
        registry.addNodeSupplier("event", (color, action) -> {
            var node = new NodeView(color, action.id(), action.name(), action.createIcon());

            //noinspection unchecked
            Map<Object, Object> map = (Map<Object, Object>) action.extraData();
            boolean cancellable = (boolean) map.get("cancellable");

            if(cancellable) {
                node.setSubtitle("Отменяемое");
            }

            return node;
        });
        registry.addNodeSupplier("game_value_getter", (color, action) -> {
            boolean selectorEnabled = true;

            if(action.extraData() != null) {
                //noinspection unchecked
                Map<Object, Object> map = (Map<Object, Object>) action.extraData();
                selectorEnabled = !(boolean) map.get("selector_disabled");
            }

            SelectorGroup selectorGroup = null;

            if(selectorEnabled) {
                selectorGroup = DataAccess.getInstance()
                        .selectorGroupRegistry
                        .get(SelectorGroup.Type.GAME_VALUE);
            }

            var node = new PureGetterNode(color, action.id(), "Игровое значение" , action.createIcon(), selectorGroup);
            node.setSubtitle(action.name());

            return node;
        });
        registry.addNodeSupplier("entity_getter", DataInitEventHandler::createEntitySelectorGetter);
        registry.addNodeSupplier("player_getter", DataInitEventHandler::createPlayerSelectorGetter);
        registry.addNodeSupplier("variable_property", (color, action) -> {
            return new PropertyGetterNode(color, action.id(), action.name(), action.createIcon());
        });
    }

    private static BPNode createEntitySelectorGetter(int color, Action action) {
        return createSelectorGetter(color, action, SelectorGroup.Type.ENTITY);
    }

    private static BPNode createPlayerSelectorGetter(int color, Action action) {
        return createSelectorGetter(color, action, SelectorGroup.Type.PLAYER);
    }

    private static BPNode createSelectorGetter(int color, Action action, SelectorGroup.Type selectorType) {
        var selectorGroup = DataAccess.getInstance()
                .selectorGroupRegistry
                .get(selectorType);

        return new SelectorVariableGetterNode(color, action.id(), action.name(), action.createIcon(), selectorGroup);
    }

    public static void onVariableTypeRegistryPreLoad(VariableTypeRegistry registry) {

    }

    public static void onContextCategoryRegistryPreLoad(ContextCategoryRegistry registry) {

    }

    public static void onSelectorGroupRegistryPreLoad(SelectorGroupRegistry registry) {

    }

    public static void onActionRegistryPreLoad(ActionRegistry registry) {
        registry.registerPinTypeFactory("enum", (entity, variableType) -> {
            //noinspection unchecked
            Map<String, String> map = (Map<String, String>) entity.extra_data;
            return new EnumPinType(entity.id, entity.label, variableType, map);
        });
    }

    public static void onDataAccessPostLoad(DataAccess dataAccess) {
        VariableTypeRegistry.registerInputPinRowCreatedListener("text", (inputPin, rowView) -> {
            rowView.addDefaultValueView(new DefaultTextInputView());
        });
        VariableTypeRegistry.registerInputPinRowCreatedListener("boolean", (inputPin, rowView) -> {
            rowView.addDefaultValueView(new DefaultBooleanInputView());
        });
        VariableTypeRegistry.registerInputPinRowCreatedListener("number", (inputPin, rowView) -> {
            rowView.addDefaultValueView(new DefaultNumberInputView());
        });
        VariableTypeRegistry.registerInputPinRowCreatedListener("enum", (inputPin, rowView) -> {
            rowView.addDefaultValueView(new DefaultEnumInputView((EnumPinType) inputPin.getType()));
        });
        VariableTypeRegistry.registerInputPinRowCreatedListener("player", (inputPin, rowView) -> {
            rowView.addDefaultValueView(createSelector(SelectorGroup.Type.PLAYER));
        });
        VariableTypeRegistry.registerInputPinRowCreatedListener("entity", (inputPin, rowView) -> {
            rowView.addDefaultValueView(createSelector(SelectorGroup.Type.ENTITY));
        });

        loadIcons(dataAccess);
    }

    private static DefaultSelectorInputView createSelector(SelectorGroup.Type type) {
        return new DefaultSelectorInputView(
                DataAccess
                .getInstance()
                .selectorGroupRegistry
                .get(type)
        );
    }

    private static void loadIcons(DataAccess dataAccess) {
        dataAccess.actionRegistry.getAll().forEach(a -> {
            var path = IconPathResolver.resolve(a);

            path = String.format("textures/%s", path);

            TextureManager.getInstance().getOrCreate(TEXTURE_NAMESPACE, path,
                    TextureManager.CACHE_MASK | TextureManager.MIPMAP_MASK);
        });
    }
}
