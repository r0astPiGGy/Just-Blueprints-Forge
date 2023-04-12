package com.rodev.jbpcore.blueprint.pin.dynamic;

import java.util.*;

public class DynamicPinHandler implements DynamicGroupResolver {

    private final List<Dynamic> childDependencies = new LinkedList<>();
    private final Map<String, DynamicGroup> groupMap = new HashMap<>();

    public void addDynamicPin(Dynamic pin) {
        if(pin.isDynamicRoot()) {
            createDynamicGroupFor(pin);
            return;
        }

        childDependencies.add(pin);
    }

    @Override
    public void addPinToGroup(String id, Dynamic pin, DynamicBehaviour behaviour) {
        groupMap.get(id).addDependency(pin, behaviour);
    }

    public void resolveDynamicDependencies() {
        childDependencies.forEach(this::findChildGroupFor);
        childDependencies.clear();
    }

    private void findChildGroupFor(Dynamic pin) {
        pin.onAddToGroupDelegate(this);
    }

    private void createDynamicGroupFor(Dynamic pin) {
        var id = pin.getType().getId();

        if(groupMap.containsKey(id)) {
            throw new IllegalArgumentException("Pin id collision: dynamic pin by id " + id + " already exists in this node.");
        }

        groupMap.put(id, pin.createDynamicGroup());
    }
}
