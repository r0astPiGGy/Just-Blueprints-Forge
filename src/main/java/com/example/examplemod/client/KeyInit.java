package com.example.examplemod.client;

import com.example.examplemod.ExampleMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.ClientRegistry;

public class KeyInit {
    public static KeyMapping OPEN_IDE_KEY;

    private KeyInit() {
    }

    public static void init() {
        OPEN_IDE_KEY = registerKey("open_ide_key", KeyMapping.CATEGORY_GAMEPLAY, InputConstants.KEY_I);
    }

    private static KeyMapping registerKey(String name, String category, int keycode) {
        final var key = new KeyMapping("key." + ExampleMod.MODID + "." + name, keycode, category);
        ClientRegistry.registerKeyBinding(key);
        return key;
    }
}
