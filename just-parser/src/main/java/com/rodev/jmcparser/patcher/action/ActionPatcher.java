package com.rodev.jmcparser.patcher.action;

import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import com.rodev.jmcparser.patcher.AbstractPatcher;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ActionPatcher extends AbstractPatcher<ActionEntity, ActionEntityPatch> {

    public ActionPatcher(ActionEntityPatch[] patches) {
        super(patches);
    }

    @Override
    protected @NotNull ActionEntity patch(ActionEntity data, ActionEntityPatch patch) {
        patchFields(data, patch);

        if(patch.input != null) {
            data.input = patchLists(data.input, patch.input);
        }

        if(patch.output != null) {
            data.output = patchLists(data.output, patch.output);
        }

        return data;
    }

    private List<ActionEntity.PinTypeEntity> patchLists(List<ActionEntity.PinTypeEntity> targetEntities, List<ActionEntityPatch.PinTypePatch> patches) {
        if(targetEntities == null) {
            return patches.stream().map(this::convert).toList();
        }

        var list = new LinkedList<ActionEntity.PinTypeEntity>();
        var targetEntitiesById = new HashMap<String, ActionEntity.PinTypeEntity>();
        targetEntities.forEach(p -> targetEntitiesById.put(p.id, p));

        var newPins = new LinkedList<ActionEntity.PinTypeEntity>();

        for(var patch : patches) {
            var patchId = patch.getPatchedId();
            var target = targetEntitiesById.get(patchId);
            if(target == null) {
                newPins.add(convert(patch));
                continue;
            }

            targetEntitiesById.remove(patchId);

            if(patch.isRemoveType()) {
                continue;
            }

            patchFields(target, patch);

            list.add(target);
        }

        list.addAll(targetEntitiesById.values());
        list.addAll(newPins);

        return list;
    }

    private ActionEntity.PinTypeEntity convert(ActionEntityPatch.PinTypePatch pinTypePatch) {
        var entity = new ActionEntity.PinTypeEntity();
        entity.id = pinTypePatch.id;
        patchFields(entity, pinTypePatch);

        return entity;
    }

    @Override
    protected String getId(ActionEntity object) {
        return object.id;
    }
}
