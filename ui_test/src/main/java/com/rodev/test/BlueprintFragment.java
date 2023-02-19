package com.rodev.test;

import com.rodev.test.blueprint.BPViewPort;
import com.rodev.test.blueprint.graph.GraphController;
import com.rodev.test.blueprint.graph.GraphControllerImpl;
import com.rodev.test.blueprint.graph.GraphLayout;
import icyllis.modernui.core.Handler;
import icyllis.modernui.core.Looper;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewConfiguration;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import static icyllis.modernui.view.View.dp;

public class BlueprintFragment extends Fragment {

    @Override
    public View onCreateView(@Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        ViewConfiguration.get().setViewScale(1);
        var base = new FrameLayout();
        {
            var aboveId = 152;
            var content = new RelativeLayout();
            {
                var upperToolPanel = new LinearLayout();
                upperToolPanel.setOrientation(LinearLayout.HORIZONTAL);
                var toolPanelParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                toolPanelParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

                upperToolPanel.setBackground(new Drawable() {
                    @Override
                    public void draw(@Nonnull Canvas canvas) {
                        Paint paint = Paint.get();
                        Rect b = getBounds();
                        paint.setRGBA(150, 0, 150, 80);
                        canvas.drawRect(b.left, b.top, b.right, b.bottom, paint);
                    }
                });

                upperToolPanel.setId(aboveId);
                {
                    var btn = new Button();
                    btn.setText("Tools");
                    upperToolPanel.addView(btn);
                }

                content.addView(upperToolPanel, toolPanelParams);
            }
            {
                var detailsPanelId = 3453;
                var body = new RelativeLayout();
                {
                    var detailsPanel = new LinearLayout();
                    {
                        var panelFiller = new TextView();
                        panelFiller.setText("Details");
                        panelFiller.setTextSize(dp(4));

                        detailsPanel.addView(panelFiller);
                    }
                    detailsPanel.setOrientation(LinearLayout.VERTICAL);
                    detailsPanel.setGravity(Gravity.CENTER | Gravity.TOP);
                    var detailsPanelParams = new RelativeLayout.LayoutParams(
                            dp(150),
                            RelativeLayout.LayoutParams.MATCH_PARENT
                    );
                    detailsPanelParams.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
                    detailsPanel.setId(detailsPanelId);

                    detailsPanel.setBackground(new Drawable() {
                        @Override
                        public void draw(@Nonnull Canvas canvas) {
                            Paint paint = Paint.get();
                            Rect b = getBounds();
                            paint.setRGBA(8, 8, 255, 80);
                            canvas.drawRect(b.left, b.top, b.right, b.bottom, paint);
                        }
                    });
                    body.addView(detailsPanel, detailsPanelParams);
                }
                {
                    var postProcessView = new FrameLayout(){
                        @Override
                        public void onDrawForeground(@NotNull Canvas canvas) {
                            super.onDrawForeground(canvas);
                            var paint = Paint.take();

                            paint.setRGBA(0, 0, 0, 80);

                            paint.setStyle(Paint.STROKE);
                            paint.setStrokeWidth(25);
                            paint.setSmoothRadius(15);

                            canvas.drawRoundLine(0, 0, getWidth(), 0, paint);
                            canvas.drawRoundLine(getWidth(), 0, getWidth(), getHeight(), paint);
                            canvas.drawRoundLine(getWidth(), getHeight(), 0, getHeight(), paint);
                            canvas.drawRoundLine(0, getHeight(), 0, 0, paint);
                        }
                    };
                    {
                        var blueprintView = new BPViewPort();
                        {
                            var graphLayout = new GraphLayout();
                            var graphController = new GraphControllerImpl(blueprintView);

                            graphController.setViewMoveListener(graphLayout);

                            graphLayout.setContextMenuOpenListener(blueprintView);
                            graphLayout.setGraphTouchListener(graphController);
                            graphLayout.setGraphController(graphController);
                            graphLayout.setDrawListener(graphController);

                            graphLayout.setMinimumHeight(Integer.MAX_VALUE);
                            graphLayout.setMinimumWidth(Integer.MAX_VALUE);

                            blueprintView.addView(graphLayout, new FrameLayout.LayoutParams(
                                    Integer.MAX_VALUE,
                                    Integer.MAX_VALUE
                            ));
                        }

                        var blueprintViewParams = new FrameLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT
                        );
                        postProcessView.addView(blueprintView, blueprintViewParams);
                    }
                    var postProcessParams = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.MATCH_PARENT
                    );
                    postProcessParams.addRule(RelativeLayout.END_OF, detailsPanelId);

                    body.addView(postProcessView, postProcessParams);
                }
                var bodyParams = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );
                bodyParams.addRule(RelativeLayout.BELOW, aboveId);
                bodyParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

                body.setBackground(new Drawable() {
                    @Override
                    public void draw(@Nonnull Canvas canvas) {
                        Paint paint = Paint.get();
                        Rect b = getBounds();
                        paint.setRGB(58, 58, 60);
                        canvas.drawRect(b.left, b.top, b.right, b.bottom, paint);
                    }
                });

                content.addView(body, bodyParams);
            }
            content.setGravity(Gravity.TOP | Gravity.CENTER);
            var params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
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
            var params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            params.setMargins(dp(5), dp(5), dp(5), dp(5));
            base.setLayoutParams(params);
        }
        return base;
    }

}
