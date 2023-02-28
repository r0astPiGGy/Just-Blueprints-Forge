package com.rodev.test;

import icyllis.modernui.graphics.font.FontFamily;
import icyllis.modernui.text.Typeface;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

import static java.awt.Font.TRUETYPE_FONT;
import static java.awt.Font.TYPE1_FONT;

public class Fonts {

    public static Typeface MINECRAFT_FONT;

    public static void init() throws IOException, FontFormatException {
        var is = Fonts.class.getResourceAsStream("fonts/Minecraft.ttf");
        if(is == null) return;

        Font f = Font.createFont(TRUETYPE_FONT, is);
        FontFamily family = new FontFamily(f);
        MINECRAFT_FONT = Typeface.createTypeface(family);

        is.close();
    }

}
