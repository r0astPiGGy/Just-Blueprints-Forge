package com.rodev.test;

import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.test.TestLinearLayout;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.ScrollView;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static icyllis.modernui.view.View.dp;

public class MainFragment extends Fragment {

    @Override
    public View onCreateView(@Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        var base = new ScrollView();
        {
            var content = new TestLinearLayout();
            var params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 25959);
            params.setMargins(dp(5), dp(5), dp(5), dp(5));
            base.addView(content, params);
        }
        base.setBackground(new Drawable() {
            @Override
            public void draw(@Nonnull Canvas canvas) {
                Paint paint = Paint.get();
                Rect b = getBounds();
                paint.setRGBA(8, 8, 8, 80);
                canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, 8, paint);
            }
        });
        {
            var params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.TOP;
            params.setMargins(dp(5), dp(5), dp(5), dp(5));
            base.setLayoutParams(params);
        }
        return base;
    }
}
