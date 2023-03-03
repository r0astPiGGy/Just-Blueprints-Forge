package com.rodev.jmcparser.json;

import com.rodev.test.blueprint.data.json.ActionEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class DataBlob {

    private final ActionData[] actions;
    private final Event[] events;
    private final GameValue[] gameValues;
    private final ActionEntity[] customActions;

}
