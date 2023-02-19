package com.rodev.test.blueprint.data.action.type;

import com.rodev.test.blueprint.data.Registry;
import com.rodev.test.blueprint.data.action.NodeSupplier;
import com.rodev.test.blueprint.data.json.ActionTypeEntity;
import com.rodev.test.utils.StringParse;
import icyllis.modernui.graphics.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionTypeRegistry extends Registry<String, ActionType> {

    private final Map<String, NodeSupplier> nodeSupplierMap = new HashMap<>();

    public void addNodeSupplier(String actionType, NodeSupplier nodeSupplier) {
        nodeSupplierMap.put(actionType, nodeSupplier);
    }

    public void load(List<ActionTypeEntity> actions) {
        data.clear();
        actions.stream().map(this::create).forEach(this::add);
    }

    private ActionType create(ActionTypeEntity entity) {
        var rawColor = entity.header_color.replace(" ", "");
        var rawRgb = rawColor.split(",");
        var rgb = new Integer[rawRgb.length];
        StringParse.parseAll(rawRgb, rgb, Integer::parseInt);

        var color = Color.rgb(rgb[0], rgb[1], rgb[2]);

        var nodeSupplier = nodeSupplierMap.getOrDefault(entity.id, NodeSupplier.identity);

        return new ActionType(entity.id, color, nodeSupplier);
    }

    private void add(ActionType actionType) {
        data.put(actionType.type(), actionType);
    }

}
