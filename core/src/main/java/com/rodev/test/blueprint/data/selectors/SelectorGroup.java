package com.rodev.test.blueprint.data.selectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

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
