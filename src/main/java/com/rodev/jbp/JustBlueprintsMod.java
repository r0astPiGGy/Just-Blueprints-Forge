package com.rodev.jbp;

import com.rodev.jbp.client.ForgeEditorEventListener;
import com.rodev.jbpcore.Fonts;
import com.rodev.jbpcore.JustBlueprints;
import com.rodev.jbpcore.data.DataAccess;
import com.rodev.jbpcore.data.DataProvider;
import com.rodev.jbpcore.workspace.ModernUIWindowManager;
import com.rodev.jbpcore.workspace.impl.WorkspaceImpl;
import icyllis.modernui.ModernUI;
import icyllis.modernui.core.Core;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.mc.forge.MuiForgeApi;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

@Mod(JustBlueprintsMod.MODID)
public class JustBlueprintsMod implements DataProvider
{
    public static final String MODID = "justblueprints";

    public JustBlueprintsMod()
    {
        DataAccess.TEXTURE_NAMESPACE = MODID;
        try {
            Fonts.loadFonts();
            JustBlueprints.setWorkspace(new WorkspaceImpl());
            JustBlueprints.setEditorEventListener(new ForgeEditorEventListener());
            JustBlueprints.setWindowManager(new ModernUIWindowManager() {
                @Override
                protected void openFragment(Fragment fragment) {
                    MuiForgeApi.openScreen(fragment);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
//        // Register the processIMC method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event){
        Core.executeOnRenderThread(()-> {
            try {
                DataAccess.load(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        System.out.println("SERVER STARTING");
    }

    @SubscribeEvent
    public void onRegistry(RegisterEvent event) {
    }

    @Override
    public InputStream getActionsInputStream() {
        return getResourceAsStream("actions.json");
    }

    @Override
    public InputStream getCategoriesInputStream() {
        return getResourceAsStream("categories.json");
    }

    @Override
    public InputStream getVariableTypesInputStream() {
        return getResourceAsStream("variable_types.json");
    }

    @Override
    public InputStream getActionTypesInputStream() {
        return getResourceAsStream("action_types.json");
    }

    @Override
    public InputStream getSelectorGroupsInputStream() {
        return getResourceAsStream("selectors.json");
    }

    @Override
    public InputStream getDataGeneratorInputStream() {
        return getResourceAsStream("generator_data.json");
    }

    @Override
    public InputStream getPinIconsInputStream() {
        return getResourceAsStream("pin_icons.json");
    }

    private InputStream getResourceAsStream(String name) {
        return JustBlueprintsMod.class.getResourceAsStream(name);
    }
}
