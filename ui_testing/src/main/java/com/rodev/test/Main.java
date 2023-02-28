package com.rodev.test;

import com.rodev.test.blueprint.data.DataAccess;
import com.rodev.test.blueprint.data.DataProvider;
import icyllis.modernui.ModernUI;
import icyllis.modernui.graphics.font.FontFamily;
import icyllis.modernui.text.Typeface;
import icyllis.modernui.view.ViewConfiguration;
import icyllis.modernui.widget.TextView;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import static java.awt.Font.TYPE1_FONT;

public class Main extends ModernUI implements DataProvider {

    public static void main(String[] args) throws IOException {
        try (Main app = new Main()) {
            Fonts.init();
            DataAccess.load(app);
            app.run(new BlueprintFragment());
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        }
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

    private InputStream getResourceAsStream(String name) {
        return Main.class.getResourceAsStream(name);
    }

    @NotNull
    @Override
    public InputStream getResourceStream(@NotNull String res, @NotNull String path) throws IOException {
        if(res.equalsIgnoreCase("actions")) {
            var resPath = "icons/actions/" + path;
            InputStream stream = getResourceAsStream(resPath);
            if (stream == null) {
                throw new FileNotFoundException(resPath);
            }
            return stream;
        } else return super.getResourceStream(res, path);
    }
}