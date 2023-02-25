package com.rodev.test.blueprint.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.test.Colors;
import com.rodev.test.blueprint.data.action.ActionRegistry;
import com.rodev.test.blueprint.data.action.type.ActionTypeRegistry;
import com.rodev.test.blueprint.data.category.ContextCategoryRegistry;
import com.rodev.test.blueprint.data.json.ActionEntity;
import com.rodev.test.blueprint.data.json.ActionTypeEntity;
import com.rodev.test.blueprint.data.json.VariableTypeEntity;
import com.rodev.test.blueprint.data.variable.DefaultInputValue;
import com.rodev.test.blueprint.data.variable.VariableTypeRegistry;
import com.rodev.test.blueprint.node.NodeView;
import com.rodev.test.blueprint.pin.exec_pin.ExecPin;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.material.MaterialCheckBox;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.CheckBox;
import icyllis.modernui.widget.EditText;
import icyllis.modernui.widget.TextView;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static icyllis.modernui.view.View.dp;
import static icyllis.modernui.view.View.sp;

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

        return cat.name;
    }

    public static DataAccess load(InputStream dataInputStream) throws IOException {
        var jdo = new ObjectMapper().readValue(dataInputStream, JsonDataObject.class);

        var actionTypeRegistry = new ActionTypeRegistry();
        actionTypeRegistry.addNodeSupplier("event", (onPinCreated, color, name) -> {
            var node = new NodeView(color, name);
            var output = ExecPin.outputPin();
            onPinCreated.accept(output);

            node.addOutput(output.createRowView());

            return node;
        });

        var variableTypeRegistry = new VariableTypeRegistry();
        var categoryRegistry = new ContextCategoryRegistry();

        actionTypeRegistry.load(jdo.action_types);
        variableTypeRegistry.load(jdo.variable_types);
        categoryRegistry.load(jdo.categories);

        var actionRegistry = new ActionRegistry(actionTypeRegistry, variableTypeRegistry);
        actionRegistry.load(jdo.actions);

        var dao = new DataAccess(actionTypeRegistry, variableTypeRegistry, categoryRegistry, actionRegistry);

        instance = dao;

        VariableTypeRegistry.registerInputPinRowCreatedListener("text", (inputPin, rowView) -> {
            rowView.addDefaultValueView(new DefaultInputValue() {
                private final EditText view = new EditText();

                {
                    view.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                    view.setHint("Текст");
                    view.setTextColor(Colors.NODE_BACKGROUND);
                    view.setBackground(new Drawable() {
                        private final int mRadius = dp(4);

                        @Override
                        public void draw(@Nonnull Canvas canvas) {
                            Paint paint = Paint.get();
                            paint.setColor(Colors.WHITE);
                            Rect b = getBounds();
                            canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, mRadius, paint);
                        }

                        @Override
                        public boolean getPadding(@Nonnull Rect padding) {
                            int r = (int) Math.ceil(mRadius / 2f);
                            padding.set(r, r, r, r);
                            return true;
                        }
                    });
                    view.setTextSize(sp(15));
                }

                @Override
                public String getDefaultValue() {
                    return view.getText().toString();
                }

                @Override
                public View asView() {
                    System.out.println("fasdfasdlfas");
                    return view;
                }
            });
        });
        VariableTypeRegistry.registerInputPinRowCreatedListener("boolean", (inputPin, rowView) -> {
            rowView.addDefaultValueView(new DefaultInputValue() {

                private final CheckBox checkBox = new MaterialCheckBox();

                @Override
                public String getDefaultValue() {
                    return String.valueOf(checkBox.isChecked());
                }

                @Override
                public View asView() {
                    return checkBox;
                }
            });
        });

        return dao;
    }

    private static class JsonDataObject {
        public List<VariableTypeEntity> variable_types = new LinkedList<>();
        public List<ActionTypeEntity> action_types = new LinkedList<>();
        public Map<String,String> categories = new HashMap<>();
        public List<ActionEntity> actions = new LinkedList<>();
    }
}
