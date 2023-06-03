package com.rodev.jbpcore.ui.view;

import icyllis.modernui.graphics.Image;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
@Setter
public class TintedImage {
    private final Image image;
    private int tint;

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }
}
