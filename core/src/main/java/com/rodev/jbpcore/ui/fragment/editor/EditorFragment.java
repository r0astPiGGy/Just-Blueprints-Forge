package com.rodev.jbpcore.ui.fragment.editor;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.ui.view.BPViewPort;
import com.rodev.jbpcore.blueprint.graph.GraphControllerImpl;
import com.rodev.jbpcore.blueprint.graph.GraphLayout;
import com.rodev.jbpcore.ui.fragment.LifecycleFragment;
import com.rodev.jbpcore.ui.drawable.ColoredBackground;
import com.rodev.jbpcore.utils.ParamsBuilder;
import com.rodev.jbpcore.utils.SquarePadding;
import com.rodev.jbpcore.ui.view.MaterialButton;
import com.rodev.jbpcore.workspace.Project;
import com.rodev.jbpcore.workspace.ModernUIWindowManager;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.view.*;
import icyllis.modernui.widget.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, DataSet savedInstanceState) {
        return createRoot();
    }

    private FrameLayout createRoot() {
        var root = new FrameLayout(getContext());

        var params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );

        params.gravity = Gravity.CENTER;
        root.setLayoutParams(params);

        ColoredBackground.of(Colors.NODE_BACKGROUND_PRIMARY).applyTo(root);

        var content = createContent();

        root.addView(content);

        return root;
    }

    private RelativeLayout createContent() {
        var content = new RelativeLayout(getContext());
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
        var toolsPanel = new LinearLayout(getContext());

        toolsPanel.setOrientation(LinearLayout.HORIZONTAL);

        ParamsBuilder.using(RelativeLayout.LayoutParams::new)
                .widthMatchParent()
                .heightWrapContent()
                .setup(p -> p.addRule(RelativeLayout.CENTER_HORIZONTAL))
                .applyTo(toolsPanel);

        SquarePadding.of(toolsPanel.dp(5))
                .applyTo(toolsPanel);

        ColoredBackground.of(Colors.TOOL_PANEL_BACKGROUND).applyTo(toolsPanel);

        toolsPanel.setId(toolsViewId);

        var saveButton = createButton("Save");
        saveButton.setOnClickListener(v -> {
            controller.onSaveButtonClicked(project, graphLayout);
        });

        ParamsBuilder.using(LinearLayout.LayoutParams::new)
                .widthWrapContent()
                .heightMatchParent()
                .setup(p -> p.rightMargin = toolsPanel.dp(10))
                .applyTo(saveButton);

        final var compileButton = new CompileButton(getContext());
        compileButton.getTextView().setText("Compile");
        controller.onCompileButtonInit(compileButton);
        compileButton.setOnClickListener(v -> {
            controller.saveProject(project, graphLayout);
            controller.onCompileButtonClicked(project, compileButton.getCompileMode());
        });

        toolsPanel.addView(saveButton);
//        toolsPanel.addView(Divider.builder().vertical().padding(dp(10)).color(Colors.NODE_BACKGROUND_PRIMARY).build());
        toolsPanel.addView(compileButton);

        return toolsPanel;
    }

    private RelativeLayout createBody() {
        var body = new RelativeLayout(getContext());

        var params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.addRule(RelativeLayout.BELOW, toolsViewId);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        body.setLayoutParams(params);
        ColoredBackground.of(Colors.NODE_BACKGROUND_SECONDARY).applyTo(body);

        var detailsPanel = createDetailsPanel();
        var viewPort = createViewPort();

        body.addView(detailsPanel);
        body.addView(viewPort);

        return body;
    }

    private LinearLayout createDetailsPanel() {
        var detailsPanel = new LinearLayout(getContext());

        detailsPanel.setId(detailsPanelId);
        detailsPanel.setOrientation(LinearLayout.VERTICAL);
        detailsPanel.setGravity(Gravity.CENTER | Gravity.TOP);

        var params = new RelativeLayout.LayoutParams(
                150,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);

        detailsPanel.setLayoutParams(params);

        ColoredBackground.of(Colors.NODE_BACKGROUND_SECONDARY_1).applyTo(detailsPanel);

        var detailsHeader = createTextView("Details");
        detailsHeader.setTextSize(detailsHeader.sp(13));

        detailsPanel.addView(detailsHeader);

        return detailsPanel;
    }

    private FrameLayout createViewPort() {
        var postProcessView = createViewPortPostProcessView();
        var blueprintView = new BPViewPort(getContext());

        var params = new FrameLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
        );
        blueprintView.setLayoutParams(params);

        var graphLayout = createGraphLayout();

        graphLayout.setNavigator(blueprintView);
        graphLayout.setContextMenuOpenHandler(blueprintView);
        graphLayout.loadBlueprint(project.getBlueprint().load(getContext()));

        blueprintView.addView(graphLayout);
        postProcessView.addView(blueprintView);

        return postProcessView;
    }

    private GraphLayout createGraphLayout() {
        var graphLayout = this.graphLayout = new GraphLayout(getContext());
        var graphController = new GraphControllerImpl();

        graphController.setViewMoveListener(graphLayout);
        graphController.setContextMenuBuilderProvider(graphLayout);

        graphLayout.setGraphController(graphController);

        graphLayout.setMinimumHeight(Integer.MAX_VALUE);
        graphLayout.setMinimumWidth(Integer.MAX_VALUE);

        return graphLayout;
    }

    private FrameLayout createViewPortPostProcessView() {
        var postProcessView = new FrameLayout(getContext()){
            @Override
            public void onDrawForeground(@NotNull Canvas canvas) {
                super.onDrawForeground(canvas);
                var paint = Paint.obtain();

                paint.setRGBA(0, 0, 0, 80);

                paint.setStyle(Paint.STROKE);
                paint.setStrokeWidth(25);

                canvas.drawLine(0, 0, getWidth(), 0, paint);
                canvas.drawLine(getWidth(), 0, getWidth(), getHeight(), paint);
                canvas.drawLine(getWidth(), getHeight(), 0, getHeight(), paint);
                canvas.drawLine(0, getHeight(), 0, 0, paint);
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
        controller.onDestroy();
    }

    @Override
    public void onLateCloseFromWindowManager(ModernUIWindowManager windowManager) {
        if(isDetached()) return;

        controller.saveProject(project, graphLayout);
    }

    private MaterialButton createButton(String text) {
        var button = new MaterialButton(getContext());
        button.getTextView().setText(text);

        return button;
    }

    private TextView createTextView(String text) {
        var textView = new TextView(getContext());
        textView.setText(text);

        return textView;
    }

}
