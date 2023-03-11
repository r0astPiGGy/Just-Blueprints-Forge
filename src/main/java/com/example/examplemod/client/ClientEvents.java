package com.example.examplemod.client;

import com.example.examplemod.ExampleMod;
//import com.rodev.test.fragment.editor.BlueprintFragment;
//import icyllis.modernui.forge.MuiForgeApi;
import com.rodev.test.JustBlueprints;
import com.rodev.test.fragment.welcome.WelcomeScreenFragment;
import icyllis.modernui.forge.MuiForgeApi;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = ExampleMod.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.OPEN_IDE_KEY.isDown()) {
                JustBlueprints.getWindowManager().open();
            }
        }
    }

    @Mod.EventBusSubscriber(modid = ExampleMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.OPEN_IDE_KEY);
        }
    }

}
