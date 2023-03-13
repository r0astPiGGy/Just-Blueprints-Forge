package com.rodev.jmcparser.patcher;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;

import static com.rodev.jmcparser.patcher.Patchable.DEFAULT_INPUT_VALUE;

public abstract class AbstractPatcher<T, P extends Patch> implements Patcher<T>{

    private final Map<String, P> patchesById = new HashMap<>();

    public AbstractPatcher(P[] patches) {
        Arrays.stream(patches).filter(Objects::nonNull).forEach(this::putPatch);
    }

    private void putPatch(P patch) {
        patchesById.put(patch.getPatchedId(), patch);
    }

    @Override
    public @Nullable T patch(T data) {
        var id = getId(data);
        var patch = patchesById.get(id);

        if(patch.isRemoveType()) {
            return null;
        }

        return patch(data, patch);
    }

    protected @NotNull T patch(T data, P patch) {
        patchFields(data, patch);

        return data;
    }

    abstract
    protected String getId(T object);

    public static <T, P extends Patch> Patcher<T> defaultPatcher(P[] data, Function<T, String> idProvider) {
        return new AbstractPatcher<>(data) {
            @Override
            protected String getId(T object) {
                return idProvider.apply(object);
            }
        };
    }

    public static void patchFields(Object target, Object patch) {
        for(Field field : patch.getClass().getFields()) {
            var patchable = field.getAnnotation(Patchable.class);

            if(patchable != null) {
                handleFieldPatch(field, target, patch, patchable);
                continue;
            }
        }
    }

    private static void handleFieldPatch(Field patchField, Object target, Object patch, Patchable annotation) {
        var fieldName = patchField.getName();
        var targetFieldName = annotation.correspondsTo();
        if(!targetFieldName.equals(DEFAULT_INPUT_VALUE)) {
            fieldName = targetFieldName;
        }

        Field targetField;

        try {
            targetField = target.getClass().getField(fieldName);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return;
        }

        Object value = null;
        try {
            value = patchField.get(patch);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if(value == null) return;

        try {
            targetField.set(target, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean shouldPatch(T data) {
        return patchesById.containsKey(getId(data));
    }
}
