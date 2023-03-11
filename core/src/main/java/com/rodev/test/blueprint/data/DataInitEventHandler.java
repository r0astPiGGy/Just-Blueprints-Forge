package com.rodev.test.blueprint.data;

import com.rodev.test.blueprint.data.action.ActionRegistry;
import com.rodev.test.blueprint.data.action.EnumPinType;
import com.rodev.test.blueprint.data.action.type.ActionTypeRegistry;
import com.rodev.test.blueprint.data.category.ContextCategoryRegistry;
import com.rodev.test.blueprint.data.selectors.SelectorGroup;
import com.rodev.test.blueprint.data.selectors.SelectorGroupRegistry;
import com.rodev.test.blueprint.data.variable.VariableTypeRegistry;
import com.rodev.test.blueprint.node.impl.NodeView;
import com.rodev.test.blueprint.node.impl.getter.PropertyGetterNode;
import com.rodev.test.blueprint.node.impl.getter.PureGetterNode;
import com.rodev.test.blueprint.node.impl.getter.SelectorVariableGetterNode;
import com.rodev.test.blueprint.pin.default_input_value.DefaultBooleanInputView;
import com.rodev.test.blueprint.pin.default_input_value.DefaultEnumInputView;
import com.rodev.test.blueprint.pin.default_input_value.DefaultNumberInputView;
import com.rodev.test.blueprint.pin.default_input_value.DefaultTextInputView;
import com.rodev.test.blueprint.pin.exec_pin.ExecPin;
import icyllis.modernui.graphics.opengl.TextureManager;

import java.io.File;
import java.util.Map;

import static com.rodev.test.blueprint.data.DataAccess.TEXTURE_NAMESPACE;

public class DataInitEventHandler {

    private DataInitEventHandler() {}

    public static void onDataPreLoad() {
        IconPathResolver.registerResolver("game_values", action -> {
            return String.format("%s%s%s.png", action.iconNamespace(), File.separator, action.id().replace("_gamevalue_getter", ""));
        });
    }

    public static void onActionTypeRegistryPreLoad(ActionTypeRegistry registry) {
        registry.addNodeSupplier("event", (color, action) -> {
            var node = new NodeView(color, action.id(), action.name(), action.createIcon());
            var output = ExecPin.outputPin();

            node.addOutputPin(output, "");

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
        registry.addNodeSupplier("entity_getter", (color, action) -> {
            var selectorGroup = DataAccess.getInstance()
                    .selectorGroupRegistry
                    .get(SelectorGroup.Type.ENTITY);

            return new SelectorVariableGetterNode(color, action.id(), action.name(), action.createIcon(), selectorGroup);
        });
        registry.addNodeSupplier("player_getter", (color, action) -> {
            var selectorGroup = DataAccess.getInstance()
                    .selectorGroupRegistry
                    .get(SelectorGroup.Type.PLAYER);

            return new SelectorVariableGetterNode(color, action.id(), action.name(), action.createIcon(), selectorGroup);
        });
        registry.addNodeSupplier("variable_property", (color, action) -> {
            return new PropertyGetterNode(color, action.id(), action.name(), action.createIcon());
        });
        registry.addNodeSupplier("pure_function", (color, action) -> {
            return new NodeView(color, action.id(), action.name(), action.createIcon());
        });
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

        loadIcons(dataAccess);
    }

    private static void loadIcons(DataAccess dataAccess) {
        dataAccess.actionRegistry.getAll().forEach(a -> {
            var path = IconPathResolver.resolve(a);

            path = String.format("textures%s%s", File.separator, path);

            TextureManager.getInstance().getOrCreate(TEXTURE_NAMESPACE, path,
                    TextureManager.CACHE_MASK | TextureManager.MIPMAP_MASK);
        });
    }
}
