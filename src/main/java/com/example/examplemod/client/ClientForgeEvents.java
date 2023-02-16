package com.example.examplemod.client;

import com.example.examplemod.ExampleMod;
import com.rodev.test.BlueprintFragment;
import icyllis.modernui.forge.MuiForgeApi;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class ClientForgeEvents {
    private ClientForgeEvents() {
    }

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if(KeyInit.OPEN_IDE_KEY.consumeClick()) {
            MuiForgeApi.openGui(new BlueprintFragment());
        }
    }
}
