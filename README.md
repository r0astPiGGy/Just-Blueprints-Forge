# JustBlueprints

---

### Описание

JustBlueprints это мод, созданный для использования на сервере JustMC. Он позволяет создавать код на блоках
в визуальном редакторе, похожем на Unreal Engine Blueprints. Сама идея мода — упростить создание кода.

Мод разрабатывается с помощью фреймворка [ModernUI](https://github.com/BloCamLimb/ModernUI).

Мод находится на стадии разработки, поэтому если нашли баг — отправляйте его [сюда](https://github.com/r0astPiGGy/Just-Blueprints-Forge/issues).

### Системные требования

- Windows 8 или выше, Linux или MacOS
- JDK 17.0.1 или выше
- OpenGL 4.5 или выше

### Установка
Для работы мода необходим загрузчик **Forge 1.19.2**.

Также для его работы нужно установить мод **[ModernUI](https://www.curseforge.com/minecraft/mc-mods/modern-ui/download)**

Установите последнюю версию JustBlueprints, загрузив его с раздела [releases](https://github.com/r0astPiGGy/Just-Blueprints-Forge/releases)

### Инструкция по сборке

Клонируем репозиторий
```shell
$ git clone https://github.com/r0astPiGGy/Just-Blueprints-Forge
$ cd Just-Blueprints-Forge/
```

Собираем проект
```shell
$ ./gradlew build
```

Переходим в директорию с собранным .jar файлом
```shell
$ cd build/libs/
```

