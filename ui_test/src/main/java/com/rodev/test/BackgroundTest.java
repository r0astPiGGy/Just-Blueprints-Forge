package com.rodev.test;

import icyllis.modernui.ModernUI;
import icyllis.modernui.core.Core;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.test.TestLinearLayout;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import org.jetbrains.annotations.Nullable;

import static org.lwjgl.opengl.GL11.*;

public class BackgroundTest extends Fragment {

    public static void main(String[] args) {
        try (ModernUI app = new ModernUI()) {
            app.run(new BackgroundTest());
        }
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        var base = new FrameLayout();
        var inputStream = BackgroundTest.class.getResourceAsStream("background_squares.png");
        var texture = MyTextureManager.getInstance().create(inputStream, true);
        var drawable = new ImageDrawable(new Image(texture));
        //drawable.setGravity(Gravity.CENTER);
        base.setBackground(drawable);
        base.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        return base;
    }
}
