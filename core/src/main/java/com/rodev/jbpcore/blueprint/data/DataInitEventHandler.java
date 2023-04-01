package com.rodev.jbpcore.blueprint.data;

import com.rodev.jbpcore.blueprint.data.action.*;
import com.rodev.jbpcore.blueprint.pin.dynamic.DynamicPinType;
import com.rodev.jbpcore.blueprint.data.action.pin_type.EnumPinType;
import com.rodev.jbpcore.blueprint.pin.exec_pin.ExecPinType;
import com.rodev.jbpcore.blueprint.data.action.pin_type.PinType;
import com.rodev.jbpcore.blueprint.data.action.type.ActionTypeRegistry;
import com.rodev.jbpcore.blueprint.data.category.ContextCategoryRegistry;
import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jbpcore.blueprint.data.selectors.SelectorGroup;
import com.rodev.jbpcore.blueprint.data.selectors.SelectorGroupRegistry;
import com.rodev.jbpcore.blueprint.data.variable.VariableType;
import com.rodev.jbpcore.blueprint.data.variable.VariableTypeRegistry;
import com.rodev.jbpcore.blueprint.node.BPNode;
import com.rodev.jbpcore.blueprint.node.impl.NodeView;
import com.rodev.jbpcore.blueprint.node.impl.getter.PropertyGetterNode;
import com.rodev.jbpcore.blueprint.node.impl.getter.PureGetterNode;
import com.rodev.jbpcore.blueprint.node.impl.getter.SelectorVariableGetterNode;
import com.rodev.jbpcore.blueprint.pin.default_input_value.*;
import com.rodev.jbpcore.blueprint.pin.list_pin.ListPinType;
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
        registry.registerPinTypeFactory("exec", DataInitEventHandler::createExecPinType);
        registry.registerPinTypeFactory("enum", DataInitEventHandler::createEnumPinType);
        registry.registerPinTypeFactory("dynamic", DataInitEventHandler::createDynamicPinType);
        registry.registerPinTypeFactory("list", (entity, variableType) -> {
            if(entity.extra_data == null) {
                throw new IllegalStateException("Element type of this list not found (" + entity.id +")");
            }

            //noinspection unchecked
            var map = (Map<String, String>) entity.extra_data;
            var elementTypeId = map.getOrDefault("element-type", "variable");

            var elementType = registry.getVariableType(elementTypeId);
            // TODO: handle dynamic list type

            return new ListPinType(entity.id, entity.label, variableType, elementType);
        });
    }

    private static PinType createExecPinType(ActionEntity.PinTypeEntity entity, VariableType variableType) {
        return new ExecPinType(entity.id, entity.label, variableType);
    }

    private static PinType createEnumPinType(ActionEntity.PinTypeEntity entity, VariableType variableType) {
        //noinspection unchecked
        var map = (Map<String, String>) entity.extra_data;
        return new EnumPinType(entity.id, entity.label, variableType, map);
    }

    private static PinType createDynamicPinType(ActionEntity.PinTypeEntity entity, VariableType variableType) {
        if(!variableType.isDynamic()) {
            throw new IllegalStateException("VariableType should be dynamic!");
        }

        String dependsOn = null;
        String dependsOnDestination = null;

        if(entity.extra_data != null) {
            //noinspection unchecked
            var map = (Map<String, String>) entity.extra_data;
            dependsOn = map.get("depends-on");
            dependsOnDestination = map.get("depend-value-dest");
        }

        return new DynamicPinType(entity.id, entity.label, variableType, dependsOn, dependsOnDestination);
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

            TextureManager.getInstance().getOrCreate(TEXTURE_NAMESPACE, path,
                    TextureManager.CACHE_MASK | TextureManager.MIPMAP_MASK);
        });
    }
}
