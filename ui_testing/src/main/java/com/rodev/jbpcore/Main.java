package com.rodev.jbpcore;

import com.rodev.jbpcore.data.DataAccess;
import com.rodev.jbpcore.data.DataProvider;
import com.rodev.jbpcore.workspace.impl.WorkspaceImpl;
import icyllis.modernui.ModernUI;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static com.rodev.jbpcore.data.DataAccess.TEXTURE_NAMESPACE;

public class Main extends ModernUI implements DataProvider {

    public static void main(String[] args) {
        try (Main app = new Main()) {
            app.init();
            JustBlueprints.getWindowManager().open();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws IOException, FontFormatException {
        Fonts.loadFonts();

        JustBlueprints.setWorkspace(new WorkspaceImpl());
        JustBlueprints.setWindowManager(new WindowManagerImpl(this));

        DataAccess.load(this);
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
        return Main.class.getResourceAsStream(name);
    }

    @NotNull
    @Override
    public InputStream getResourceStream(@NotNull String namespace, @NotNull String path) throws IOException {
        if(!namespace.equalsIgnoreCase(TEXTURE_NAMESPACE)) {
            return super.getResourceStream(namespace, path);
        }

        InputStream stream = getResourceAsStream(path);
        if (stream == null) {
            throw new FileNotFoundException(path);
        }

        return stream;
    }
}