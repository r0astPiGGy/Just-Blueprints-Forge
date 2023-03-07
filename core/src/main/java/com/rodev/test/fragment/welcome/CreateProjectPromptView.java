package com.rodev.test.fragment.welcome;

import com.rodev.test.Colors;
import com.rodev.test.utils.ColoredBackground;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.*;
import lombok.Setter;

import static com.rodev.test.Fonts.MINECRAFT_FONT;
import static icyllis.modernui.view.MeasureSpec.EXACTLY;
import static icyllis.modernui.view.MeasureSpec.makeMeasureSpec;

public class CreateProjectPromptView extends RelativeLayout {

    private final static int HEADER_ID = 1355334;
    private final static int FOOTER_ID = 1354321;

    @Setter
    private Runnable onDeclineListener = () -> {};

    @Setter
    private Runnable onAcceptListener = () -> {};

    public CreateProjectPromptView() {
        ColoredBackground.of(Colors.NODE_BACKGROUND).applyTo(this);

        var params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.gravity = Gravity.CENTER;

        setLayoutParams(params);

        addView(createBody());
        addView(createHeader());
        addView(createFooter());
    }

    private TextView createHeader() {
        var textView = new TextView();
        textView.setId(HEADER_ID);

        textView.setText("Создать Новый Проект");
        textView.setTextSize(View.sp(24));
        var params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(ALIGN_PARENT_TOP);
        textView.setLayoutParams(params);

        textView.setPadding(dp(5), dp(5), dp(5), dp(5));
        textView.setTypeface(MINECRAFT_FONT);
        ColoredBackground.of(Colors.NODE_BACKGROUND_SECONDARY).applyTo(textView);

        return textView;
    }

    private LinearLayout createBody() {
        var body = new LinearLayout();

        var params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );
        params.addRule(BELOW, HEADER_ID);
        params.addRule(ABOVE, FOOTER_ID);

        body.setLayoutParams(params);
        ColoredBackground.of(Colors.WHITE).applyTo(body);

        return body;
    }

    private RelativeLayout createFooter() {
        var footer = new RelativeLayout();
        footer.setId(FOOTER_ID);

        var params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.addRule(ALIGN_PARENT_BOTTOM);
        footer.setLayoutParams(params);

        ColoredBackground.of(Colors.VECTOR_COLOR).applyTo(footer);

        addView(createDeclineButton());
        addView(createAcceptButton());

        return footer;
    }

    private Button createDeclineButton() {
        var button = new Button();

        var params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        params.addRule(ALIGN_PARENT_BOTTOM);
        params.addRule(ALIGN_PARENT_LEFT);

        button.setLayoutParams(params);

        button.setOnClickListener(v -> {
            onDeclineListener.run();
        });

        button.setText("Отмена");
        button.setTextSize(sp(20));
        button.setTypeface(MINECRAFT_FONT);
        button.setTextColor(Colors.NODE_BACKGROUND);
        ColoredBackground.of(Colors.VECTOR_COLOR).applyTo(button);

        return button;
    }

    private Button createAcceptButton() {
        var button = new Button();

        var params = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        params.addRule(ALIGN_PARENT_BOTTOM);
        params.addRule(ALIGN_PARENT_RIGHT);

        button.setLayoutParams(params);

        button.setOnClickListener(v -> {
            onAcceptListener.run();
        });

        button.setText("Создать");
        button.setTextSize(sp(20));
        button.setTypeface(MINECRAFT_FONT);
        button.setTextColor(Colors.NODE_BACKGROUND);
        ColoredBackground.of(Colors.VECTOR_COLOR).applyTo(button);

        return button;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        var width = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.50);
        var height = (int) (MeasureSpec.getSize(heightMeasureSpec) * 0.50);

        super.onMeasure(makeMeasureSpec(width, EXACTLY), makeMeasureSpec(height, EXACTLY));
    }

}
