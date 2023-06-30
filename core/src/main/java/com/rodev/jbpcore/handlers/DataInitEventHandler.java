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
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.ImageStore;

import java.util.Map;

public class DataInitEventHandler {

    private DataInitEventHandler() {}

    public static void onDataPreLoad() {
        IconPathResolver.registerResolver("game_values", action -> {
            return String.format("%s/%s.png", action.iconNamespace(), action.id().replace("_gamevalue_getter", ""));
        });
    }

    public static void onActionTypeRegistryPreLoad(ActionTypeRegistry registry) {
        registry.addNodeSupplier("event", (context, color, action) -> {
            var node = new NodeView(context, color, action.id(), action.name(), action.createIcon());

            //noinspection unchecked
            Map<Object, Object> map = (Map<Object, Object>) action.extraData();
            boolean cancellable = (boolean) map.get("cancellable");

            if(cancellable) {
                node.setSubtitle("Отменяемое");
            }

            return node;
        });
        registry.addNodeSupplier("game_value_getter", (context, color, action) -> {
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

            var node = new PureGetterNode(context, color, action.id(), "Игровое значение" , action.createIcon(), selectorGroup);
            node.setSubtitle(action.name());

            return node;
        });
        registry.addNodeSupplier("simple_function", DataInitEventHandler::createSimpleFunction);
        registry.addNodeSupplier("entity_getter", DataInitEventHandler::createEntitySelectorGetter);
        registry.addNodeSupplier("player_getter", DataInitEventHandler::createPlayerSelectorGetter);
        registry.addNodeSupplier("variable_property", (context, color, action) -> {
            return new PropertyGetterNode(context, color, action.id(), action.name(), action.createIcon());
        });
    }

    private static GraphNode createSimpleFunction(Context context, int color, Action action) {
        return new NodeView(context, color, action.id(), action.name(), action.createIcon());
    }

    private static GraphNode createEntitySelectorGetter(Context context, int color, Action action) {
        return createSelectorGetter(context, color, action, SelectorGroup.Type.ENTITY);
    }

    private static GraphNode createPlayerSelectorGetter(Context context, int color, Action action) {
        return createSelectorGetter(context, color, action, SelectorGroup.Type.PLAYER);
    }

    private static GraphNode createSelectorGetter(Context context, int color, Action action, SelectorGroup.Type selectorType) {
        var selectorGroup = DataAccess.getInstance()
                .selectorGroupRegistry
                .get(selectorType);

        return new SelectorVariableGetterNode(context, color, action.id(), action.name(), action.createIcon(), selectorGroup);
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
                    rowView.addDefaultValueView(new DefaultTextInputView(rowView.getContext()));
                })
                .addListener("boolean", (inputPin, rowView) -> {
                    rowView.addDefaultValueView(new DefaultBooleanInputView(rowView.getContext()));
                })
                .addListener("number", (inputPin, rowView) -> {
                    rowView.addDefaultValueView(new DefaultNumberInputView(rowView.getContext()));
                })
                .addListener("enum", (inputPin, rowView) -> {
                    rowView.addDefaultValueView(new DefaultEnumInputView(
                            rowView.getContext(),
                            (EnumPinType) inputPin.getType())
                    );
                })
                .addListener("player", (inputPin, rowView) -> {
                    rowView.addDefaultValueView(createSelector(rowView.getContext(), SelectorGroup.Type.PLAYER));
                })
                .addListener("entity", (inputPin, rowView) -> {
                    rowView.addDefaultValueView(createSelector(rowView.getContext(), SelectorGroup.Type.ENTITY));
                })
                .registerAll();

        loadIcons(dataAccess);
    }

    private static DefaultSelectorInputView createSelector(Context context, SelectorGroup.Type type) {
        return new DefaultSelectorInputView(
                context,
                DataAccess
                .getInstance()
                .selectorGroupRegistry
                .get(type)
        );
    }

    private static void loadIcons(DataAccess dataAccess) {
        dataAccess.actionRegistry.getAll().forEach(a -> {
            var path = IconPathResolver.resolve(a);

            //Image.create(DataAccess.TEXTURE_NAMESPACE, path);
        });
    }
}
