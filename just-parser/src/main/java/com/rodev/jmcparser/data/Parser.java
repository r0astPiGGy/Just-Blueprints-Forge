package com.rodev.jmcparser.data;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public abstract class Parser<T, I extends Interpreter<T>> {

    private Consumer<T[]> onDataChanged = data -> {};
    private Consumer<I> onInterpreterCreated = i -> {};

    private T[] parsedData;

    protected void setParsedData(T[] parsedData) {
        this.parsedData = parsedData;
        onDataChanged.accept(parsedData);
    }

    public void setOnDataChangedListener(Consumer<T[]> onDataChangedListener) {
        onDataChanged = onDataChangedListener;
    }

    public void setOnInterpreterCreatedListener(Consumer<I> onInterpreterCreated) {
        this.onInterpreterCreated = onInterpreterCreated;
    }

    public Interpreter<T> createInterpreter() {
        var interpreter = createInterpreter(parsedData);
        onInterpreterCreated.accept(interpreter);

        return interpreter;
    }

    public abstract void load(DataProvider dataProvider);

    protected abstract I createInterpreter(T[] parsedData);

    public static <T> T parseJson(InputStream is, Class<T> clazz) {
        try {
            return new ObjectMapper().readValue(is, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
