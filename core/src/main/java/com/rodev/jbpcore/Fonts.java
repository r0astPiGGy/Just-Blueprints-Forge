package com.rodev.jbpcore;

import icyllis.modernui.graphics.font.FontFamily;
import icyllis.modernui.text.Typeface;

import java.awt.*;
import java.io.IOException;

import static java.awt.Font.TRUETYPE_FONT;

public class Fonts {

    public static Typeface MINECRAFT_FONT;

    public static void loadFonts() throws IOException, FontFormatException {
        var inputStream = Fonts.class.getResourceAsStream("fonts/Minecraft.ttf");
        if(inputStream == null) return;

        var font = Font.createFont(TRUETYPE_FONT, inputStream);
        var family = new FontFamily(font);
        MINECRAFT_FONT = Typeface.createTypeface(family);

        inputStream.close();
    }

}
