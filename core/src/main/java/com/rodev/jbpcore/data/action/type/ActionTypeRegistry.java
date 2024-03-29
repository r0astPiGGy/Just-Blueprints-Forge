package com.rodev.jbpcore.data.action.type;

import com.rodev.jbpcore.data.Registry;
import com.rodev.jbpcore.data.action.NodeSupplier;
import com.rodev.jbpcore.data.json.ActionTypeEntity;
import com.rodev.jbpcore.utils.StringParse;
import icyllis.modernui.graphics.Color;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ActionTypeRegistry extends Registry<String, ActionType> {

    private final Map<String, NodeSupplier> nodeSupplierMap = new HashMap<>();

    public void addNodeSupplier(String actionType, NodeSupplier nodeSupplier) {
        nodeSupplierMap.put(actionType, nodeSupplier);
    }

    public void load(ActionTypeEntity[] actions) {
        data.clear();
        Arrays.stream(actions).map(this::create).forEach(this::add);
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
