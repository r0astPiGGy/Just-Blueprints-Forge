package com.rodev.jbpcore.ui.fragment.welcome;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.data.DataAccess;
import com.rodev.jbpcore.ui.fragment.LifecycleFragment;
import com.rodev.jbpcore.ui.drawable.ColoredBackground;
import com.rodev.jbpcore.utils.ParamsBuilder;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.util.DataSet;
import icyllis.modernui.util.DisplayMetrics;
import icyllis.modernui.view.*;
import icyllis.modernui.widget.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

import static com.rodev.jbpcore.Fonts.MINECRAFT_FONT;
import static com.rodev.jbpcore.data.DataAccess.TEXTURE_NAMESPACE;
import static com.rodev.jbpcore.utils.ViewUtils.matchParent;
import static icyllis.modernui.view.MeasureSpec.EXACTLY;
import static icyllis.modernui.view.MeasureSpec.makeMeasureSpec;

public class WelcomeScreenFragment extends LifecycleFragment {

    private final int recentProjectsId = 15433;

    private final WelcomeScreenController welcomeScreenController = new WelcomeScreenController(this);

    public static int getBackgroundCornerRadius(View view) {
        return view.dp(20);
    }

    private View root;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, DataSet savedInstanceState) {
//        var r = getContext().getResources();
//        var metrics = r.getDisplayMetrics();
//
//        metrics.density = DisplayMetrics.DENSITY_MEDIUM;
//        metrics.scaledDensity = DisplayMetrics.DENSITY_DEFAULT_SCALE;
//
//        r.updateMetrics(metrics);

        var rootContainer = new FrameLayout(getContext());
        matchParent(rootContainer);

        var root = new RelativeLayout(getContext()) {

            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                var width = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.65);
                var height = (int) (MeasureSpec.getSize(heightMeasureSpec) * 0.70);

                super.onMeasure(makeMeasureSpec(width, EXACTLY), makeMeasureSpec(height, EXACTLY));
            }
        };

        var params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        params.gravity = Gravity.CENTER;

        root.setLayoutParams(params);

        root.addView(createWelcomeCard());
        root.addView(createRecentProjectsContainer());

        rootContainer.addView(root);

        this.root = rootContainer;

        return rootContainer;
    }

    private LinearLayout createRecentProjectsContainer() {
        var container = new LinearLayout(getContext());
        container.setOrientation(LinearLayout.VERTICAL);
        container.setId(recentProjectsId);

        var params = new RelativeLayout.LayoutParams(
                container.dp(400), ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        ColoredBackground.of(Colors.NODE_BACKGROUND_SECONDARY)
                .setDrawFunction((canvas, b, paint) -> {
                    canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, getBackgroundCornerRadius(container), Gravity.RIGHT, paint);
                })
                .applyTo(container);

        container.setLayoutParams(params);
        container.setPadding(
                container.dp(5),
                container.dp(5),
                container.dp(5),
                container.dp(5)
        );

        container.addView(createRecentProjectsScrollView());

        return container;
    }

    private ScrollView createRecentProjectsScrollView() {
        var recentProjectsScrollView = new ScrollView(getContext());
        var params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );

        recentProjectsScrollView.setLayoutParams(params);
        recentProjectsScrollView.addView(createRecentProjects());

        return recentProjectsScrollView;
    }

    private RecentProjectsView createRecentProjects() {
        var view = new RecentProjectsView(getContext());
        view.init();
        view.setOnItemClick(welcomeScreenController::onRecentProjectClicked);

        return view;
    }

    private FrameLayout createWelcomeCard() {
        var welcomeCard = new FrameLayout(getContext());
        var params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        );

        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        params.addRule(RelativeLayout.START_OF, recentProjectsId);

        ColoredBackground.of(Colors.NODE_BACKGROUND)
                .setDrawFunction((canvas, b, paint) -> {
                    canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, getBackgroundCornerRadius(welcomeCard), Gravity.LEFT, paint);
                })
                .applyTo(welcomeCard);
        welcomeCard.setLayoutParams(params);

        welcomeCard.addView(welcomeCardContents());

        return welcomeCard;
    }

    private LinearLayout welcomeCardContents() {
        var content = new LinearLayout(getContext());
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER);

        var params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        content.setLayoutParams(params);

        content.addView(iconImage());
        content.addView(headerText());
        content.addView(buttons());

        return content;
    }

    private FrameLayout iconImage() {
        var image = new FrameLayout(getContext());

        var params = new LinearLayout.LayoutParams(image.dp(175), image.dp(175));
        params.bottomMargin = image.dp(30);
        image.setLayoutParams(params);

        var imageDrawable = DataAccess.createImageDrawable("ui", "logo");
        image.setBackground(imageDrawable);

        return image;
    }

    private LinearLayout headerText() {
        var content = new LinearLayout(getContext());
        content.setOrientation(LinearLayout.VERTICAL);
        content.setGravity(Gravity.CENTER | Gravity.TOP);

        var params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = content.dp(100);
        content.setLayoutParams(params);

        content.addView(titleText());
        content.addView(subtitleText());

        return content;
    }

    private TextView titleText() {
        var text = new TextView(getContext());

        text.setText("JustBlueprints");
        text.setTextSize(text.sp(45));
        text.setTypeface(MINECRAFT_FONT);

        var params = wrapContent(text);
        params.bottomMargin = text.dp(6);

        return text;
    }

    private TextView subtitleText() {
        var text = new TextView(getContext());

        text.setText("made by r0astPiGGy");
        text.setTextSize(text.sp(25));
        text.setTextColor(Colors.NODE_BACKGROUND_PRIMARY);
        text.setTypeface(MINECRAFT_FONT);

        wrapContent(text);

        return text;
    }

    private LinearLayout buttons() {
        var content = new LinearLayout(getContext());

        content.setOrientation(LinearLayout.VERTICAL);
        wrapContent(content);

        content.addView(createProjectButton());
        content.addView(createButton("Открыть", "ic-folder"));

        return content;
    }

    private View createProjectButton() {
        var createProject = createButton("Создать Новый Проект", "ic-plus");
        createProject.setOnClickListener(this::onCreateProjectButtonClicked);

        return createProject;
    }

    private void onCreateProjectButtonClicked(View button) {
        var popup = new CreateProjectPopup(root);

        popup.setOnPromptCreated(this::onCreateProjectPromptCreated);

        popup.show();
    }

    private void onCreateProjectPromptCreated(CreateProjectPromptView prompt) {
        prompt.setNameValidator(welcomeScreenController::validateProjectName);
        prompt.setOnAcceptListener(welcomeScreenController::onProjectCreated);
    }

    private LinearLayout createButton(String text, String icon) {
        var layout = new LinearLayout(getContext()) {
            @Override
            public PointerIcon onResolvePointerIcon(@Nonnull MotionEvent event) {
                if (isClickable() && isEnabled()) {
                    return PointerIcon.getSystemIcon(PointerIcon.TYPE_HAND);
                }
                return super.onResolvePointerIcon(event);
            }
        };
        layout.setGravity(Gravity.CENTER | Gravity.LEFT);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        ParamsBuilder.using(LinearLayout.LayoutParams::new)
                .wrapContent()
                .setup(p -> p.bottomMargin = layout.dp(20))
                .applyTo(layout);

        var iconDrawable = new ImageDrawable(TEXTURE_NAMESPACE, "ui/" + icon + ".png");

        var iconView = new View(getContext());
        iconView.setBackground(iconDrawable);

        ParamsBuilder.using(LinearLayout.LayoutParams::new)
                .squareShape(layout.dp(30))
                .setup(p -> p.rightMargin = layout.dp(15))
                .applyTo(iconView);

        var textView = new TextView(getContext());

        textView.setText(text);
        textView.setTextSize(layout.sp(20));
        textView.setTypeface(MINECRAFT_FONT);
        wrapContent(textView).bottomMargin = layout.dp(3);

        layout.addView(iconView);
        layout.addView(textView);

        return layout;
    }

    private static LinearLayout.LayoutParams wrapContent(View view) {
        var params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);

        return params;
    }
}
