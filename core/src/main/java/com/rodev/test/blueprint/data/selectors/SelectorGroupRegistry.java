package com.rodev.test.blueprint.data.selectors;

import com.rodev.test.blueprint.data.Registry;
import com.rodev.test.blueprint.data.json.SelectorGroupEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class SelectorGroupRegistry extends Registry<String, SelectorGroup> {

    public void load(SelectorGroupEntity[] selectorGroupEntities) {
        Arrays.stream(selectorGroupEntities).map(this::create).forEach(this::add);
    }

    private SelectorGroup create(SelectorGroupEntity selectorGroupEntity) {
        checkIfSelectorGroupValid(selectorGroupEntity);

        var selectors = selectorGroupEntity.selectors
                .entrySet()
                .stream()
                .map(entry -> new Selector(entry.getKey(), entry.getValue()))
                .collect(ArrayList<Selector>::new, ArrayList::add, ArrayList::addAll);

        return new SelectorGroup(selectorGroupEntity.id, selectors);
    }

    private void checkIfSelectorGroupValid(SelectorGroupEntity entity) {
        var groupId = entity.id;

        for (var groupType : SelectorGroup.Type.values()) {
            var groupTypeId = groupType.getId();

            if(entity.id.equals(groupTypeId))
                return;
        }

        throw new IllegalArgumentException("Unknown selector group type provided: " + groupId);
    }

    private void add(SelectorGroup selectorGroup) {
        data.put(selectorGroup.id(), selectorGroup);
    }

    public SelectorGroup get(SelectorGroup.Type type) {
        return get(type.getId());
    }

}
