package com.rodev.jbpcore.data;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ObjectType {
    NUMBER("Число"),
    TEXT("Текст"),
    LOCATION("Местоположение"),
    ITEM("Предмет"),
    POTION("Зелье"),
    SOUND("Звук"),
    PARTICLE("Частица"),
    VECTOR("Вектор"),
    LIST("Список"),
    MAP("Словарь")

    ;

    public final String name;
}
