package com.rodev.jbpcore.blueprint.data.selectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

public record SelectorGroup(String id, List<Selector> selectors) {

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        GAME_VALUE("game_value"),
        PLAYER("player"),
        ENTITY("entity")

        ;

        private final String id;
    }

}
