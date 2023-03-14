package com.rodev.jmcparser.data;

import com.rodev.jmcparser.patcher.Patcher;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public abstract class DataWriter<T, P> {

    private final JsonDataWriter jsonDataWriter;

    private Patcher<P> patcher;

    public DataWriter(File file) {
        jsonDataWriter = new JsonDataWriter(file);
    }

    public abstract void write(T[] data);

    protected void writeToFile(Object object) {
        jsonDataWriter.write(object);
    }

    public void setPatcher(Patcher<P> patcher) {
        this.patcher = patcher;
    }

    @Nullable
    protected P patch(P object) {
        if(patcher == null) return object;

        if(!patcher.shouldPatch(object)) return object;

        return patcher.patch(object);
    }

    public static <T> Default<T> defaultDataWriter(File file) {
        return new Default<>(file);
    }

    public static class Default<T> extends DataWriter<T, T> {

        private Default(File file) {
            super(file);
        }

        @Override
        public void write(T[] data) {
            var list = Arrays.stream(data)
                    .map(this::patch)
                    .filter(Objects::nonNull)
                    .toList();

            writeToFile(list);
        }
    }
}
