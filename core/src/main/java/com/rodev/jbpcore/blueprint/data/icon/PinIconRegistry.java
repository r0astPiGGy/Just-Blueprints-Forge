package com.rodev.jbpcore.blueprint.data.icon;

import com.rodev.jbpcore.blueprint.data.DataAccess;
import com.rodev.jbpcore.blueprint.data.Registry;
import com.rodev.jbpcore.blueprint.data.json.PinIconEntity;

import java.util.Arrays;

public class PinIconRegistry extends Registry<String, PinIcon> {

    public void load(PinIconEntity[] entities) {
        data.clear();
        Arrays.stream(entities)
                .map(this::create)
                .forEach(this::add);
    }

    private PinIcon create(PinIconEntity entity) {
        var id = entity.id;
        var icon = DataAccess.createImage("ui", entity.icon);
        var connected = DataAccess.createImage("ui", entity.connectedIcon);

        return new PinIcon(id, icon, connected);
    }

    private void add(PinIcon icon) {
        data.put(icon.id(), icon);
    }

}
