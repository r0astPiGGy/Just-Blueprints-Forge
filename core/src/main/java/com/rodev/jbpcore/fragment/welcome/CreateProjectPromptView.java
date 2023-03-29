package com.rodev.jbpcore.fragment.welcome;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.utils.ColoredBackground;
import com.rodev.jbpcore.utils.ParamsBuilder;
import com.rodev.jbpcore.utils.RoundedColoredBackground;
import com.rodev.jbpcore.view.DefaultTextWatcher;
import com.rodev.jbpcore.view.LabeledEditText;
import com.rodev.jbpcore.view.MaterialButton;
import icyllis.modernui.text.Editable;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.*;
import icyllis.modernui.widget.Button;
import lombok.Setter;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.rodev.jbpcore.Fonts.MINECRAFT_FONT;
import static icyllis.modernui.view.MeasureSpec.EXACTLY;
import static icyllis.modernui.view.MeasureSpec.makeMeasureSpec;

public class CreateProjectPromptView extends RelativeLayout {

    private final static int HEADER_ID = 1355334;
    private final static int FOOTER_ID = 1354321;
    private final static int BODY_ID = 3255245;

    private EditText editText;

    @Setter
    private ProjectNameValidator nameValidator = (s, r) -> {};

    @Setter
    private Runnable onDeclineListener = () -> {};

    @Setter
    private OnCreateButtonClicked onAcceptListener = projectName -> {};

    public CreateProjectPromptView() {
        RoundedColoredBackground.builder()
                .color(Colors.NODE_BACKGROUND)
                .paddingOffset(dp(15))
                .radius(dp(10))
                .build()
                .applyTo(this);

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

        textView.setTypeface(MINECRAFT_FONT);

        return textView;
    }
    
    private RelativeLayout createBody() {
        var body = new LabeledEditText();

        body.setLabel("Название:");
        body.setId(BODY_ID);

        ParamsBuilder.using(RelativeLayout.LayoutParams::new)
                .wrapContent()
                .setup(params -> {
                    params.addRule(CENTER_IN_PARENT);
                    params.addRule(CENTER_VERTICAL);
                    params.addRule(CENTER_HORIZONTAL);
                }).applyTo(body);

        // ватафак, эти три строчки кода фиксят баг с диалогом (нет блин монолог)
        // nvm, it doesn't
        body.setOnEditTextCreatedListener(materialEditText -> {
            editText = materialEditText;
            ParamsBuilder.using(LinearLayout.LayoutParams::new)
                    .heightWrapContent()
                    .width(dp(350))
                    .setup(p -> {
                        p.leftMargin = dp(10);
                    })
                    .applyTo(materialEditText);
            materialEditText.addTextChangedListener(new DefaultTextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    onNameEditTextChanged(body, s);
                }
            });
            materialEditText.setHint("Введите текст...");
            materialEditText.setMinimumWidth(dp(350));
            materialEditText.setSingleLine();
            materialEditText.setMaxWidth(dp(350));
        });

        body.fill();

        return body;
    }

    private final ValidateResult lastInputResult = new ValidateResult(false, null);

    private void onNameEditTextChanged(LabeledEditText parent, Editable editText) {
        var input = editText.toString();
        lastInputResult.restore();
        nameValidator.accept(input, lastInputResult);

        if(lastInputResult.isSuccess()) {
            parent.hideErrorMessage();
        } else {
            parent.setErrorMessage(lastInputResult.getErrorMessage());
            parent.showErrorMessage();
        }
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

    private MaterialButton createDeclineButton() {
        var button = new MaterialButton(Colors.BUTTON_COLOR_PRIMARY);

        ParamsBuilder.using(RelativeLayout.LayoutParams::new)
                .wrapContent()
                .setup(params -> {
                    params.addRule(ALIGN_PARENT_BOTTOM);
                    params.addRule(ALIGN_PARENT_LEFT);
                })
                .applyTo(button);

        button.setOnClickListener(v -> {
            onDeclineListener.run();
        });
        button.setText("Отмена");

        return button;
    }

    private MaterialButton createAcceptButton() {
        var button = new MaterialButton(Colors.SELECTED_COLOR);

        ParamsBuilder.using(RelativeLayout.LayoutParams::new)
                .wrapContent()
                .setup(params -> {
                    params.addRule(ALIGN_PARENT_BOTTOM);
                    params.addRule(ALIGN_PARENT_RIGHT);
                })
                .applyTo(button);

        button.setOnClickListener(v -> {
            if(lastInputResult.isSuccess()) {
                onAcceptListener.accept(editText.getText().toString());
            }
        });

        button.setText("Создать");

        return button;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        var width = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.50);
        var height = (int) (MeasureSpec.getSize(heightMeasureSpec) * 0.50);

        super.onMeasure(makeMeasureSpec(width, EXACTLY), makeMeasureSpec(height, EXACTLY));
    }

    public interface ProjectNameValidator extends BiConsumer<String, ValidateResult> { }

    public interface OnCreateButtonClicked extends Consumer<String> {}

}
