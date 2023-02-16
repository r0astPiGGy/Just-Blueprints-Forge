package com.rodev.test;

import com.rodev.test.blueprint.graph.GraphLayout;
import icyllis.modernui.ModernUI;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.*;
import org.jetbrains.annotations.Nullable;

import static icyllis.modernui.view.View.sp;

public class ViewPortTest extends Fragment {

    public static void main(String[] args) {
        try (ModernUI app = new ModernUI()) {
            app.run(new ViewPortTest());
        }
    }

    private float scaleFactor = 1;

    @Nullable
    @Override
    public View onCreateView(@Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        var base = new ScrollView();
        {
            var linearLayout = new LinearLayout();
            {
                var graphLayout = new GraphLayout();
                {
                    var text = new TextView();
                    text.setText("Lorem ipsum dolor sit amet");
                    text.setTextSize(sp(25));

                    graphLayout.add(text);
                }
                var drawable = new ImageDrawable(ViewPortTest.class.getResourceAsStream("background_squares.png"));
                //drawable.setGravity(Gravity.CENTER);
                graphLayout.setBackground(drawable);

                linearLayout.addView(graphLayout, new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        2000
                ));
            }
            base.addView(linearLayout, new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            ));
        }
        base.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        return base;
    }
}
