package com.rodev.jmcparser.data.game_value;

import com.rodev.jmcparser.data.LocaleProvider;
import com.rodev.jmcparser.json.GameValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GameValueTranslator {

    private final GameValue[] gameValues;
    private final LocaleProvider localeProvider;

    public static void translate(GameValue[] gameValues, LocaleProvider localeProvider) {
        new GameValueTranslator(gameValues, localeProvider).translate();
    }

    public void translate() {
        Arrays.stream(gameValues).forEach(this::translate);
    }

    private void translate(GameValue gameValue) {
        var key = String.format("creative_plus.game_value.%s.name", gameValue.id);

        gameValue.name = localeProvider.translateKeyOrDefault(key);
    }

}
