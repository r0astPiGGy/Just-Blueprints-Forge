package com.rodev.jbpcore.view;

import com.rodev.jbpcore.Colors;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.material.MaterialDrawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.util.StateSet;
import icyllis.modernui.widget.Button;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static com.rodev.jbpcore.Fonts.MINECRAFT_FONT;

public class MaterialButton extends Button {

    private static final ColorStateList TINT_LIST = createColorStateList(Colors.BUTTON_COLOR_PRIMARY);

    public static ColorStateList createColorStateList(int nodeColor) {
        return new ColorStateList(
                new int[][]{
//                        StateSet.get(StateSet.VIEW_STATE_SELECTED),
//                        StateSet.get(StateSet.VIEW_STATE_HOVERED),
                        StateSet.get(StateSet.VIEW_STATE_ENABLED),
                        StateSet.WILD_CARD},
                new int[]{
//                        Colors.WHITE,
//                        Colors.WHITE,
                        nodeColor,
                        Colors.BUTTON_DISABLED}
        );
    }

    private MaterialButton(ColorStateList stateList) {
        var background = new Background(dp(5));
        background.setTintList(stateList);
        setBackground(background);
        setTextAlignment(TEXT_ALIGNMENT_CENTER);
        setMinWidth(dp(140));

        setTypeface(MINECRAFT_FONT);
        setTextSize(sp(20));
    }

    public MaterialButton(int color) {
        this(createColorStateList(color));
    }

    public MaterialButton() {
        this(TINT_LIST);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if(enabled) {
            setTextColor(Colors.WHITE);
        } else {
            setTextColor(Colors.BUTTON_TEXT_DISABLED);
        }
    }

    private static class Background extends MaterialDrawable {

        int padding = 0;
        int radius;

        public Background(int radius) {
            this.radius = radius;
        }

        @Override
        public void draw(@NotNull Canvas canvas) {
            var b = getBounds();
            var p = Paint.get();

            p.setColor(mColor);
            p.setAlpha(modulateAlpha(p.getAlpha(), mAlpha));

            if(p.getAlpha() == 0) return;

            canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, radius, p);
        }

        @Override
        public boolean getPadding(@Nonnull Rect padding) {
            var r = calcPadding() + dp(5);
            padding.set(r, r, r, r);

            return true;
        }

        protected int calcPadding() {
            return this.padding = (int) Math.ceil(radius / 2f);
        }
    }

}
