package com.rodev.test.utils;

import com.rodev.test.Colors;
import com.rodev.test.Fonts;
import icyllis.modernui.graphics.font.FontPaint;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.TextView;

import static icyllis.modernui.graphics.font.FontPaint.BOLD;
import static icyllis.modernui.graphics.font.FontPaint.ITALIC;

public class TextViewCreationListener {

    private TextViewCreationListener() {
        throw new IllegalStateException("why");
    }

    public static void onNodeTitleCreated(TextView textView) {
        setDefaultFont(textView);
        textView.setTextStyle(BOLD);
        textView.setTextSize(View.sp(16));
    }

    public static void onNodeSubtitleCreated(TextView textView) {
        setDefaultFont(textView);
        textView.setTextStyle(ITALIC);
        textView.setTextSize(View.sp(14));
        textView.setTextColor(Colors.NODE_SUBTITLE_COLOR);
    }

    public static void onPinTextCreated(TextView textView) {
        setDefaultFont(textView);
    }

    public static void onContextMenuItemTextCreated(TextView textView) {
        setDefaultFont(textView);
        textView.setTextSize(View.sp(14));
    }

    public static void onContextMenuHeaderTextCreated(TextView textView) {
        setDefaultFont(textView);
        textView.setTextSize(View.sp(17));
    }

    public static void onContextMenuSearchViewCreated(TextView textView) {
        setDefaultFont(textView);
        textView.setTextSize(View.sp(13));
        textView.setTextColor(Colors.NODE_BACKGROUND);
    }

    public static void onContextMenuCategoryTextCreated(TextView textView) {
        setDefaultFont(textView);
        textView.setTextStyle(FontPaint.BOLD);
        textView.setTextSize(View.sp(13));
    }

    private static void setDefaultFont(TextView textView) {
        textView.setTypeface(Fonts.MINECRAFT_FONT);
    }

}
