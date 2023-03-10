package com.rodev.jbpcore.fragment.editor;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.blueprint.BPViewPort;
import com.rodev.jbpcore.blueprint.graph.GraphControllerImpl;
import com.rodev.jbpcore.blueprint.graph.GraphLayout;
import com.rodev.jbpcore.fragment.LifecycleFragment;
import com.rodev.jbpcore.view.MaterialButton;
import com.rodev.jbpcore.workspace.Project;
import com.rodev.jbpcore.workspace.WindowManager;
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

import static icyllis.modernui.view.View.sp;

public class EditorFragment extends LifecycleFragment {

    private final int toolsViewId = 152;
    private final int detailsPanelId = 3453;

    private final EditorController controller = new EditorController();
    private GraphLayout graphLayout;

    private final Project project;

    public EditorFragment(Project project) {
        this.project = project;
    }

    @Override
    public View onCreateView(@Nullable ViewGroup container, @Nullable DataSet savedInstanceState) {
        ViewConfiguration.get().setViewScale(1);

        return createRoot();
    }

    private FrameLayout createRoot() {
        var root = new FrameLayout();

        var params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );

        params.gravity = Gravity.CENTER;
        root.setLayoutParams(params);

        root.setBackground(new Drawable() {
            @Override
            public void draw(@Nonnull Canvas canvas) {
                Paint paint = Paint.get();
                Rect b = getBounds();
                paint.setRGBA(8, 8, 8, 80);
                canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, 8, paint);
            }
        });

        var content = createContent();

        root.addView(content);

        return root;
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

        var params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);

        toolsPanel.setLayoutParams(params);

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
        var saveButton = createButton("Save");
        saveButton.setOnClickListener(v -> {
            controller.onSaveButtonClicked(project, graphLayout);
        });

        toolsPanel.addView(button);
        toolsPanel.addView(saveButton);

        return toolsPanel;
    }

    private RelativeLayout createBody() {
        var body = new RelativeLayout();

        var params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.addRule(RelativeLayout.BELOW, toolsViewId);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        body.setLayoutParams(params);

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
                150,
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
        detailsHeader.setTextSize(sp(8));

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
        graphLayout.loadBlueprint(project.getBlueprint());

        blueprintView.addView(graphLayout);
        postProcessView.addView(blueprintView);

        return postProcessView;
    }

    private GraphLayout createGraphLayout() {
        var graphLayout = this.graphLayout = new GraphLayout();
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
        var params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.addRule(RelativeLayout.END_OF, detailsPanelId);
        postProcessView.setLayoutParams(params);
        return postProcessView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        controller.saveProject(project, graphLayout);
    }

    @Override
    public void onLateCloseFromWindowManager(WindowManager windowManager) {
        if(isDetached()) return;

        controller.saveProject(project, graphLayout);
    }

    private Button createButton(String text) {
        var button = new MaterialButton();
        button.setText(text);
        button.setBackgroundColor(Colors.SELECTED_COLOR);

        return button;
    }

    private TextView createTextView(String text) {
        var textView = new TextView();
        textView.setText(text);

        return textView;
    }

}
