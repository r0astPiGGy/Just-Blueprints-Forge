package com.rodev.jbpcore;

import java.util.regex.Pattern;

public class MessageTest {

    public static void main(String[] args) {
        var message = """
                Успех  Файл загружен

                 Используйте данную команду на сервере для загрузки модуля:
                 /module loadUrl force https://m.justmc.ru/api/9897aaa5

                 Важно  Модуль по ссылке удалится через 3 минуты!
                        Успейте использовать команду на сервере за данное время
                        """;

        var regex = "/module.+\n";

        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(message);

        String tempCommand = "null";

        if(matcher.find()) {
            tempCommand = matcher.group();
        }

        var command = tempCommand;

        var split = message.split(regex);

        System.out.println(split[0] + command + split[1]);
    }

}
