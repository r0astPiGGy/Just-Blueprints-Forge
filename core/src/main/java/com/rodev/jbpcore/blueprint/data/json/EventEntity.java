package com.rodev.jbpcore.blueprint.data.json;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class EventEntity extends ActionEntity {

    private EventEntity(boolean cancellable) {
        extra_data = new EventData(cancellable);
        type = "event";
    }

    public static EventEntity create(boolean cancellable) {
        return new EventEntity(cancellable);
    }

    @AllArgsConstructor
    @NoArgsConstructor
    public static class EventData {
        public boolean cancellable;
    }

}
