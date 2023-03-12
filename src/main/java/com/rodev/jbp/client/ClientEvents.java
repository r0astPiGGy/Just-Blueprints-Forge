package com.rodev.jbp.client;

import com.rodev.jbp.JustBlueprintsMod;
//import com.rodev.test.fragment.editor.BlueprintFragment;
//import icyllis.modernui.forge.MuiForgeApi;
import com.rodev.jbpcore.JustBlueprints;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = JustBlueprintsMod.MODID, value = Dist.CLIENT)
    public static class ClientForgeEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if(KeyBinding.OPEN_IDE_KEY.isDown()) {
                JustBlueprints.getWindowManager().open();
            }
        }
    }

    @Mod.EventBusSubscriber(modid = JustBlueprintsMod.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBinding.OPEN_IDE_KEY);
        }
    }

}
