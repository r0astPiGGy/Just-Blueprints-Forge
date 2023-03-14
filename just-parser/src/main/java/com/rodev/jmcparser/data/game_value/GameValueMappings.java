package com.rodev.jmcparser.data.game_value;

import com.rodev.jmcparser.data.DataProvider;
import com.rodev.jmcparser.data.Parser;
import com.rodev.jmcparser.json.GameValueMappedEntity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GameValueMappings {

    private final Map<String, String> mappedIds = new HashMap<>();

    public void load(DataProvider dataProvider) {
        dataProvider.loadMappedGameValuesAndApply(this::load);
    }

    private void load(InputStream inputStream) {
        var mappings = Parser.parseJson(inputStream, GameValueMappedEntity[].class);

        for (var mapping : mappings) {
            mappedIds.put(mapping.mappedId, mapping.realId);
        }
    }

    public String getRealId(String mappedId) {
        return mappedIds.getOrDefault(mappedId, mappedId);
    }

}
