package com.example.examplemod.client;

import com.example.examplemod.ExampleMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    private final static String KEY_CATEGORY = "key.category." + ExampleMod.MODID;
    private final static String KEY_OPEN_IDE = "key." + ExampleMod.MODID + ".open_ide";
    public static final KeyMapping OPEN_IDE_KEY = new KeyMapping(
            KEY_OPEN_IDE,
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_I,
            KEY_CATEGORY
    );

    private KeyBinding() {}
}
