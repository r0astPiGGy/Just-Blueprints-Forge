package com.rodev.jmcparser.data.action.alternate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodev.jmcparser.ActionCategories;
import com.rodev.jmcparser.util.TimeCounter;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class AlternateActionProvider {

    private final Map<String, AlternateAction> alternateActionMap = new HashMap<>();

    public void load(InputStream inputStream) {
        var counter = new TimeCounter();

        var objMapper = new ObjectMapper();

        AlternateAction[] data;

        try {
            data = objMapper.readValue(inputStream, AlternateAction[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Arrays.stream(data)
                .filter(Objects::nonNull)
                .forEach(a -> alternateActionMap.put(a.id, a));

        counter.print(estimated -> "Loaded " + alternateActionMap.size() + " alternate actions in " + estimated + "ms.");
    }

    @Nullable
    public AlternateAction getById(String id) {
        return alternateActionMap.get(id);
    }

    public Collection<AlternateAction> getAll() {
        return alternateActionMap.values();
    }

}
