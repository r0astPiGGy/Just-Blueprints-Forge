package com.rodev.test.blueprint.data.variable;

import com.rodev.test.blueprint.data.Registry;
import com.rodev.test.blueprint.data.json.VariableTypeEntity;
import com.rodev.test.utils.StringParse;
import icyllis.modernui.graphics.Color;

import java.util.List;

public class VariableTypeRegistry extends Registry<String, VariableType> {

    public void load(List<VariableTypeEntity> variableTypeList) {
        data.clear();
        variableTypeList.stream().map(this::create).forEach(this::add);
    }

    private VariableType create(VariableTypeEntity entity) {
        var rawColor = entity.color.replace(" ", "");
        var rawRgb = rawColor.split(",");
        var rgb = new Integer[rawRgb.length];
        StringParse.parseAll(rawRgb, rgb, Integer::parseInt);

        var color = Color.rgb(rgb[0], rgb[1], rgb[2]);

        return new VariableType(entity.id, color);
    }

    private void add(VariableType variableType) {
        data.put(variableType.type(), variableType);
    }

}
