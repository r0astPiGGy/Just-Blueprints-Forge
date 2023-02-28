package com.rodev.jmcparser.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LocaleProvider {

    @NotNull
    String translateKeyOrDefault(String key);

    @Nullable
    String translateKey(String key);

}
