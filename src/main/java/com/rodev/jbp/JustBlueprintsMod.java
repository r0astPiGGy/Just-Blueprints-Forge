package com.rodev.jbp;

import com.rodev.jbpcore.Fonts;
import com.rodev.jbpcore.JustBlueprints;
import com.rodev.jbpcore.blueprint.data.DataAccess;
import com.rodev.jbpcore.blueprint.data.DataProvider;
import com.rodev.jbpcore.workspace.WindowManager;
import com.rodev.jbpcore.workspace.impl.WorkspaceImpl;
import icyllis.modernui.forge.MuiForgeApi;
import icyllis.modernui.fragment.Fragment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;

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
            DataAccess.load(this);

            JustBlueprints.setWorkspace(new WorkspaceImpl());
            JustBlueprints.setWindowManager(new WindowManager() {
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

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
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

    private InputStream getResourceAsStream(String name) {
        return JustBlueprintsMod.class.getResourceAsStream(name);
    }
}
