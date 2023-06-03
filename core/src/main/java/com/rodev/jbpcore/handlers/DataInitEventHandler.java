package com.rodev.jbpcore.handlers;

import com.rodev.jbpcore.data.DataAccess;
import com.rodev.jbpcore.data.IconPathResolver;
import com.rodev.jbpcore.data.action.pin_type.EnumPinType;
import com.rodev.jbpcore.data.action.type.ActionTypeRegistry;
import com.rodev.jbpcore.data.category.ContextCategoryRegistry;
import com.rodev.jbpcore.data.selectors.SelectorGroup;
import com.rodev.jbpcore.data.selectors.SelectorGroupRegistry;
import com.rodev.jbpcore.data.variable.VariableTypeRegistry;
import com.rodev.jbpcore.blueprint.node.GraphNode;
import com.rodev.jbpcore.data.action.Action;
import com.rodev.jbpcore.data.action.ActionRegistry;
import com.rodev.jbpcore.ui.node.NodeView;
import com.rodev.jbpcore.ui.node.getter.PropertyGetterNode;
import com.rodev.jbpcore.ui.node.getter.PureGetterNode;
import com.rodev.jbpcore.ui.node.getter.SelectorVariableGetterNode;
import com.rodev.jbpcore.ui.pin.default_input_value.*;
import icyllis.modernui.graphics.opengl.TextureManager;

import java.util.Map;

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
        registry.addNodeSupplier("simple_function", DataInitEventHandler::createSimpleFunction);
        registry.addNodeSupplier("entity_getter", DataInitEventHandler::createEntitySelectorGetter);
        registry.addNodeSupplier("player_getter", DataInitEventHandler::createPlayerSelectorGetter);
        registry.addNodeSupplier("variable_property", (color, action) -> {
            return new PropertyGetterNode(color, action.id(), action.name(), action.createIcon());
        });
    }

    private static GraphNode createSimpleFunction(int color, Action action) {
        return new NodeView(color, action.id(), action.name(), action.createIcon());
    }

    private static GraphNode createEntitySelectorGetter(int color, Action action) {
        return createSelectorGetter(color, action, SelectorGroup.Type.ENTITY);
    }

    private static GraphNode createPlayerSelectorGetter(int color, Action action) {
        return createSelectorGetter(color, action, SelectorGroup.Type.PLAYER);
    }

    private static GraphNode createSelectorGetter(int color, Action action, SelectorGroup.Type selectorType) {
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
        PinTypeFactoryHandler.of(registry).register();
    }

    public static void onDataAccessPostLoad(DataAccess dataAccess) {
        VariableTypeRegistry.inputPinRowCreatedListenerBuilder()
                .addListener("text", (inputPin, rowView) -> {
                    rowView.addDefaultValueView(new DefaultTextInputView());
                })
                .addListener("boolean", (inputPin, rowView) -> {
                    rowView.addDefaultValueView(new DefaultBooleanInputView());
                })
                .addListener("number", (inputPin, rowView) -> {
                    rowView.addDefaultValueView(new DefaultNumberInputView());
                })
                .addListener("enum", (inputPin, rowView) -> {
                    rowView.addDefaultValueView(new DefaultEnumInputView((EnumPinType) inputPin.getType()));
                })
                .addListener("player", (inputPin, rowView) -> {
                    rowView.addDefaultValueView(createSelector(SelectorGroup.Type.PLAYER));
                })
                .addListener("entity", (inputPin, rowView) -> {
                    rowView.addDefaultValueView(createSelector(SelectorGroup.Type.ENTITY));
                })
                .registerAll();

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

            TextureManager.getInstance().getOrCreate(DataAccess.TEXTURE_NAMESPACE, path,
                    TextureManager.CACHE_MASK | TextureManager.MIPMAP_MASK);
        });
    }
}
