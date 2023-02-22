package com.rodev.test;

import com.rodev.test.blueprint.BPViewPort;
import com.rodev.test.blueprint.graph.GraphControllerImpl;
import com.rodev.test.blueprint.graph.GraphLayout;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
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

    private final int toolsViewId = 152;
    private final int detailsPanelId = 3453;

    @Override
    public View onCreateView(@Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        ViewConfiguration.get().setViewScale(1);

        return createRoot();
    }

    private FrameLayout createRoot() {
        var base = new FrameLayout();

        var params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );

        params.gravity = Gravity.CENTER;
        params.setMargins(dp(5), dp(5), dp(5), dp(5));
        base.setLayoutParams(params);

        base.setBackground(new Drawable() {
            @Override
            public void draw(@Nonnull Canvas canvas) {
                Paint paint = Paint.get();
                Rect b = getBounds();
                paint.setRGBA(8, 8, 8, 80);
                canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, 8, paint);
            }
        });

        var content = createContent();

        base.addView(content);

        return base;
    }

    private RelativeLayout createContent() {
        var content = new RelativeLayout();
        content.setGravity(Gravity.TOP | Gravity.CENTER);

        var params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );

        content.setLayoutParams(params);

        var toolsPanel = createToolsPanel();
        var body = createBody();

        content.addView(toolsPanel);
        content.addView(body);

        return content;
    }

    private LinearLayout createToolsPanel() {
        var toolsPanel = new LinearLayout();

        toolsPanel.setOrientation(LinearLayout.HORIZONTAL);

        var toolPanelParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        toolPanelParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

        toolsPanel.setLayoutParams(toolPanelParams);

        toolsPanel.setBackground(new Drawable() {
            @Override
            public void draw(@Nonnull Canvas canvas) {
                Paint paint = Paint.get();
                Rect b = getBounds();
                paint.setRGBA(150, 0, 150, 80);
                canvas.drawRect(b.left, b.top, b.right, b.bottom, paint);
            }
        });

        toolsPanel.setId(toolsViewId);

        var button = createButton("Tools");
        toolsPanel.addView(button);

        return toolsPanel;
    }

    private RelativeLayout createBody() {
        var body = new RelativeLayout();

        var bodyParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        bodyParams.addRule(RelativeLayout.BELOW, toolsViewId);
        bodyParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        body.setLayoutParams(bodyParams);

        body.setBackground(new Drawable() {
            @Override
            public void draw(@Nonnull Canvas canvas) {
                Paint paint = Paint.get();
                Rect b = getBounds();
                paint.setRGB(58, 58, 60);
                canvas.drawRect(b.left, b.top, b.right, b.bottom, paint);
            }
        });

        var detailsPanel = createDetailsPanel();
        var viewPort = createViewPort();

        body.addView(detailsPanel);
        body.addView(viewPort);

        return body;
    }

    private LinearLayout createDetailsPanel() {
        var detailsPanel = new LinearLayout();

        detailsPanel.setId(detailsPanelId);
        detailsPanel.setOrientation(LinearLayout.VERTICAL);
        detailsPanel.setGravity(Gravity.CENTER | Gravity.TOP);

        var params = new RelativeLayout.LayoutParams(
                dp(150),
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        detailsPanel.setLayoutParams(params);

        detailsPanel.setBackground(new Drawable() {
            @Override
            public void draw(@Nonnull Canvas canvas) {
                Paint paint = Paint.get();
                Rect b = getBounds();
                paint.setRGBA(8, 8, 255, 80);
                canvas.drawRect(b.left, b.top, b.right, b.bottom, paint);
            }
        });

        var detailsHeader = createTextView("Details");
        detailsHeader.setTextSize(dp(8));

        detailsPanel.addView(detailsHeader);

        return detailsPanel;
    }

    private FrameLayout createViewPort() {
        var postProcessView = createViewPortPostProcessView();
        var blueprintView = new BPViewPort();

        var params = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        blueprintView.setLayoutParams(params);

        var graphLayout = createGraphLayout();

        graphLayout.setNavigator(blueprintView);
        graphLayout.setContextMenuOpenHandler(blueprintView);

        blueprintView.addView(graphLayout);
        postProcessView.addView(blueprintView);

        return postProcessView;
    }

    private GraphLayout createGraphLayout() {
        var graphLayout = new GraphLayout();
        var graphController = new GraphControllerImpl();

        graphController.setViewMoveListener(graphLayout);
        graphController.setContextMenuBuilderProvider(graphLayout);

        graphLayout.setGraphController(graphController);

        graphLayout.setMinimumHeight(Integer.MAX_VALUE);
        graphLayout.setMinimumWidth(Integer.MAX_VALUE);

        return graphLayout;
    }

    private FrameLayout createViewPortPostProcessView() {
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
        var postProcessParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        postProcessParams.addRule(RelativeLayout.END_OF, detailsPanelId);
        postProcessView.setLayoutParams(postProcessParams);
        return postProcessView;
    }

    private Button createButton(String text) {
        var btn = new Button();
        btn.setText(text);

        return btn;
    }

    private TextView createTextView(String text) {
        var textView = new TextView();
        textView.setText(text);

        return textView;
    }


}
