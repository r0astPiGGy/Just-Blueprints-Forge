package com.rodev.jmcparser.generator;

import com.rodev.jbpcore.data.json.ActionEntity;

import java.util.function.BiConsumer;

public interface OnActionInterpretedListener<T> extends BiConsumer<ActionEntity, T> {}
