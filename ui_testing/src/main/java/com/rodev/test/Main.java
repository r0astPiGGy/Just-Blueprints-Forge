package com.rodev.test;

import com.rodev.test.blueprint.data.DataProvider;
import com.rodev.test.fragment.welcome.WelcomeScreenFragment;
import com.rodev.test.impl.WorkspaceImpl;
import com.rodev.test.workspace.ProgramData;
import com.rodev.test.workspace.Project;
import com.rodev.test.workspace.Workspace;
import com.rodev.test.workspace.impl.ProjectImpl;
import icyllis.modernui.ModernUI;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import static com.rodev.test.blueprint.data.DataAccess.TEXTURE_NAMESPACE;

public class Main extends ModernUI implements DataProvider {

    public static void main(String[] args) throws IOException {
        try (Main app = new Main()) {
            app.init();
            app.run(new WelcomeScreenFragment());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void init() throws IOException, FontFormatException {
        Fonts.loadFonts();
//      DataAccess.load(app);

        JustBlueprints.setWorkspace(new WorkspaceImpl());
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