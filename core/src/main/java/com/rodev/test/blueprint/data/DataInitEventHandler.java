package com.rodev.test.blueprint.data;

import com.rodev.test.blueprint.data.action.ActionRegistry;
import com.rodev.test.blueprint.data.action.EnumPinType;
import com.rodev.test.blueprint.data.action.type.ActionTypeRegistry;
import com.rodev.test.blueprint.data.category.ContextCategoryRegistry;
import com.rodev.test.blueprint.data.variable.VariableTypeRegistry;
import com.rodev.test.blueprint.node.NodeView;
import com.rodev.test.blueprint.pin.default_input_value.DefaultBooleanInputView;
import com.rodev.test.blueprint.pin.default_input_value.DefaultEnumInputView;
import com.rodev.test.blueprint.pin.default_input_value.DefaultNumberInputView;
import com.rodev.test.blueprint.pin.default_input_value.DefaultTextInputView;
import com.rodev.test.blueprint.pin.exec_pin.ExecPin;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.opengl.TextureManager;

import java.util.Map;

public class DataInitEventHandler {

    private DataInitEventHandler() {}

    public static void onActionTypeRegistryPreLoad(ActionTypeRegistry registry) {
        registry.addNodeSupplier("event", (color, action) -> {
            var node = new NodeView(color, action.id(), action.name());
            var output = ExecPin.outputPin();

            node.addOutput(output.createRowView());
            //noinspection unchecked
            Map<Object, Object> map = (Map<Object, Object>) action.extraData();
            boolean cancellable = (boolean) map.get("cancellable");

            if(cancellable) {
                node.setSubTitle("Отменяемое");
            }

            return node;
        });
        registry.addNodeSupplier("pure-function", (color, action) -> new NodeView(color, action.id(), action.name()));
    }

    public static void onVariableTypeRegistryPreLoad(VariableTypeRegistry registry) {

    }

    public static void onContextCategoryRegistryPreLoad(ContextCategoryRegistry registry) {

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
            TextureManager.getInstance().getOrCreate("actions", "textures/" + a.id() + ".png",
                    TextureManager.CACHE_MASK | TextureManager.MIPMAP_MASK);
        });
    }
}
