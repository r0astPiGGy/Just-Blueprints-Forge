package com.rodev.test.blueprint.data.selectors;

import com.rodev.test.blueprint.data.Registry;
import com.rodev.test.blueprint.data.json.SelectorGroupEntity;

import java.util.Arrays;
import java.util.stream.Collectors;

public class SelectorGroupRegistry extends Registry<String, SelectorGroup> {

    public void load(SelectorGroupEntity[] selectorGroupEntities) {
        Arrays.stream(selectorGroupEntities).map(this::create).forEach(this::add);
    }

    private SelectorGroup create(SelectorGroupEntity selectorGroupEntity) {
        var selectors = selectorGroupEntity.selectors
                .entrySet()
                .stream()
                .map(entry -> new Selector(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());

        return new SelectorGroup(selectorGroupEntity.id, selectors);
    }

    private void add(SelectorGroup selectorGroup) {
        data.put(selectorGroup.id(), selectorGroup);
    }

}
