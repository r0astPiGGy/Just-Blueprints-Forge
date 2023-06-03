package com.rodev.jbpcore.ui.view;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.ui.drawable.ColoredBackground;
import com.rodev.jbpcore.utils.ParamsBuilder;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.LinearLayout;

public class Divider extends View {

    private Divider() {}

    private static Divider createHorizontal(int height, int padding) {
        var view = new Divider();

        ParamsBuilder.using(LinearLayout.LayoutParams::new)
                .height(height)
                .widthMatchParent()
                .setup(p -> p.setMargins(0, padding, 0, padding))
                .applyTo(view);

        return view;
    }

    private static Divider createVertical(int width, int padding) {
        var view = new Divider();

        ParamsBuilder.using(LinearLayout.LayoutParams::new)
                .width(width)
                .heightMatchParent()
                .setup(p -> p.setMargins(padding, 0, padding, 0))
                .applyTo(view);

        return view;
    }

    public static DividerBuilder builder() {
        return new DividerBuilder();
    }

    public static class DividerBuilder {

        private boolean vertical = true;
        private int size = dp(1);
        private int color = Colors.NODE_BACKGROUND_SECONDARY;
        private int padding = 0;

        public DividerBuilder vertical() {
            vertical = true;
            return this;
        }

        public DividerBuilder horizontal() {
            vertical = false;
            return this;
        }

        public DividerBuilder size(int dp) {
            size = dp;
            return this;
        }

        public DividerBuilder color(int color) {
            this.color = color;
            return this;
        }

        public DividerBuilder padding(int padding) {
            this.padding = padding;
            return this;
        }

        public Divider build() {
            var view = vertical ? createVertical(size, padding) : createHorizontal(size, padding);

            ColoredBackground.of(color).applyTo(view);

            return view;
        }

    }

}
