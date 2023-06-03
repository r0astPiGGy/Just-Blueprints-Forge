package com.rodev.jbpcore.ui.fragment.welcome;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.JustBlueprints;
import com.rodev.jbpcore.utils.ParamsBuilder;
import com.rodev.jbpcore.utils.ViewUtils;
import com.rodev.jbpcore.workspace.Project;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.text.TextUtils;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.PointerIcon;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.TextView;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.function.Consumer;

import static com.rodev.jbpcore.Colors.SELECTED_COLOR;
import static com.rodev.jbpcore.Fonts.MINECRAFT_FONT;
import static com.rodev.jbpcore.data.DataAccess.*;

public class RecentProjectsView extends LinearLayout {

    private Consumer<Project> onItemClick = p -> {};

    private ProjectView selectedProject;

    public RecentProjectsView() {
        var params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        setPadding(dp(10), 0, dp(10), 0);
        setLayoutParams(params);
        setOrientation(VERTICAL);
    }

    public void setOnItemClick(Consumer<Project> clickListener) {
        onItemClick = clickListener;
    }

    private void onProjectDelete(ProjectView projectView) {
        var programData = JustBlueprints.getWorkspace().getProgramData();

        programData.removeRecentProject(projectView.project.getName());
        programData.save();

        removeView(projectView);
    }

    private void onProjectClick(ProjectView projectView) {
        onItemClick.accept(projectView.project);
    }

    public void init() {
        JustBlueprints.getWorkspace()
                .getProgramData()
                .getRecentProjects()
                .stream()
                .sorted(Comparator.comparingLong(Project::getLastOpenDate)
                        .reversed())
                .toList()
                .forEach(this::createAndAddProject);
    }

    private void createAndAddProject(Project project) {
        addView(createProjectView(project));
    }

    private View createProjectView(Project project) {
        return new ProjectView(project);
    }

    private class ProjectView extends RelativeLayout {

        private boolean selected;
        private TextView pathView;
        private final CrossView crossButton = new CrossView();
        public final Project project;

        private ProjectView(Project project) {
            this.project = project;

            var params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, dp(10), 0, dp(10));

            ProjectView.this.setLayoutParams(params);

            ProjectView.this.setPadding(dp(20), dp(5), dp(5), dp(5));

            var nameAndPath = createNameAndPath();

            crossButton.disable();
            crossButton.setOnClickListener(v -> {
                onProjectDelete(this);
            });
            ParamsBuilder.using(RelativeLayout.LayoutParams::new)
                    .squareShape(dp(15))
                    .setup(p -> {
                        p.addRule(RelativeLayout.ALIGN_PARENT_END);
                        p.addRule(RelativeLayout.CENTER_VERTICAL);
                        p.rightMargin = dp(3);
                    }).applyTo(crossButton);

            addView(nameAndPath);
            addView(crossButton);

            setOnClickListener(this::onClick);

            setBackground(new Background());
        }

        private LinearLayout createNameAndPath() {
            var textContainer = new LinearLayout();
            textContainer.setOrientation(LinearLayout.VERTICAL);

            ParamsBuilder.using(RelativeLayout.LayoutParams::new)
                    .heightWrapContent()
                    .widthMatchParent()
                    .setup(params -> {
                        params.addRule(RelativeLayout.ALIGN_PARENT_START);
                        params.addRule(START_OF, crossButton.getId());
                    })
                    .applyTo(textContainer);

            var name = createText();
            name.setText(project.getName());
            ParamsBuilder.using(LinearLayout.LayoutParams::new)
                    .heightWrapContent()
                    .widthMatchParent()
                    .applyTo(name);

            pathView = createText();
            pathView.setText(project.getDirectory().getPath());
            pathView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            pathView.setSingleLine();
            pathView.setTextSize(sp(20));
            pathView.setTextColor(Colors.NODE_BACKGROUND_PRIMARY);
            ParamsBuilder.using(LinearLayout.LayoutParams::new)
                    .heightWrapContent()
                    .widthMatchParent()
                    .applyTo(pathView);

            textContainer.addView(name);
            textContainer.addView(pathView);

            return textContainer;
        }

        private TextView createText() {
            var textView = new TextView();
            ViewUtils.matchParentWrapContent(textView);
            textView.setTextSize(sp(24));
            textView.setTypeface(MINECRAFT_FONT);

            return textView;
        }

        private void onClick(View view) {
            if(isProjectSelected()) {
                onProjectClick(this);
                return;
            }

            select();
        }

        public void select() {
            if(selectedProject != null) {
                selectedProject.deselect();
            }

            selectedProject = this;

            selected = true;
            pathView.setTextColor(Colors.WHITE);
            crossButton.enable();

            ProjectView.this.invalidate();
        }

        public void deselect() {
            selected = false;
            pathView.setTextColor(Colors.NODE_BACKGROUND_PRIMARY);
            crossButton.disable();
            ProjectView.this.invalidate();
        }

        public boolean isProjectSelected() {
            return selected;
        }

        @Override
        public PointerIcon onResolvePointerIcon(@Nonnull MotionEvent event) {
            if (isClickable() && isEnabled()) {
                return PointerIcon.getSystemIcon(PointerIcon.TYPE_HAND);
            }
            return super.onResolvePointerIcon(event);
        }

        private class Background extends Drawable {
            private final float mRadius = dp(5);

            @Override
            public void draw(@NotNull Canvas canvas) {
                if(isProjectSelected()) {
                    var b = getBounds();
                    var p = Paint.get();
                    p.setColor(SELECTED_COLOR);
                    canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, mRadius, p);
                }
            }

            @Override
            public boolean getPadding(@Nonnull Rect padding) {
                int r = (int) Math.ceil(mRadius / 1.5f);
                padding.set(dp(10), r, dp(10), r);
                return true;
            }
        }
    }

    private static class CrossView extends View {

        private boolean hovering;

        public CrossView() {
            // TODO draw two lines instead of using icon
            var crossImage = new ImageDrawable(TEXTURE_NAMESPACE, getPath("ui", "ic-cross")) {
                @Override
                public void draw(@NotNull Canvas canvas) {
                    if(hovering) {
                        var b = getBounds();
                        var p = Paint.get();
                        p.setColor(Colors.NODE_BACKGROUND);
                        canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, b.width(), p);
                    }
                    super.draw(canvas);
                }
            };
            setId(12341325);
            setBackground(crossImage);
        }

        @Override
        public boolean onHoverEvent(@NotNull MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_HOVER_EXIT) {
                onHoverExit();
            } else if (event.getAction() == MotionEvent.ACTION_HOVER_ENTER){
                onHoverEnter();
            }

            return super.onHoverEvent(event);
        }

        public void onHoverEnter() {
            hovering = true;
            invalidate();
        }

        public void onHoverExit() {
            hovering = false;
            invalidate();
        }

        public void enable() {
            setVisibility(View.VISIBLE);
            setEnabled(true);
        }

        public void disable() {
            setVisibility(View.INVISIBLE);
            setEnabled(false);
        }
    }
}
