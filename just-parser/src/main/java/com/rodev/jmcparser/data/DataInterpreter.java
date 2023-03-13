package com.rodev.jmcparser.data;

import com.rodev.jmcparser.util.TimeCounter;
import com.rodev.jbpcore.blueprint.data.json.ActionEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DataInterpreter extends Interpreter<Void> {

    private final List<Interpreter<?>> interpreters = new LinkedList<>();

    public DataInterpreter() {
        super(null);
    }

    public ActionEntity[] interpret() {
        var counter = new TimeCounter();

        List<ActionEntity[]> data = new ArrayList<>();

        var totalSize = 0;

        for(var interpreter : interpreters) {
            var interpreted = interpreter.interpret();
            totalSize += interpreted.length;
            data.add(interpreted);
        }

        var actions = new ActionEntity[totalSize];
        var index = 0;
        for(var bulk : data) {
            for (ActionEntity entity : bulk) {
                actions[index] = entity;
                index++;
            }
        }

        counter.print(ms -> "Interpreted " + actions.length + " actions in " + ms + "ms.");

        return actions;
    }

    @Override
    protected @Nullable ActionEntity interpret(@NotNull Void object) {
        return null;
    }

    public void registerInterpreter(Interpreter<?> interpreter) {
        interpreters.add(interpreter);
    }
}
