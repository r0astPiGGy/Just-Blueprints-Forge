package com.rodev.jbpcore.view;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.utils.ParamsBuilder;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.EditText;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.TextView;
import lombok.Setter;

import java.util.function.Consumer;

import static com.rodev.jbpcore.Fonts.MINECRAFT_FONT;

public class LabeledEditText extends RelativeLayout {

    @Setter
    private String label = "EditText:";

    @Setter
    private Consumer<TextView> onTextCreatedListener = t -> {};
    @Setter
    private Consumer<MaterialEditText> onEditTextCreatedListener = t -> {};

    private static final int LABEL_ID = 234135545;
    private static final int EDIT_TEXT_ID = 3435441;

    private TextView errorMessage;
    private MaterialEditText editText;

    public LabeledEditText() {
        setGravity(Gravity.TOP);
    }

    public void fill() {
        addView(createTextView());
        addView(createEditText());
        addView(createErrorTextView());
    }

    public void setErrorMessage(String text) {
        errorMessage.setText(text);
    }

    public void showErrorMessage() {
        if(errorMessage == null) {
            return;
        }

        errorMessage.setVisibility(View.VISIBLE);
        editText.setOutlineColor(Colors.RED);
    }

    public void hideErrorMessage() {
        if(errorMessage == null) {
            return;
        }

        errorMessage.setVisibility(View.GONE);
        editText.setOutlineColor(Colors.WHITE);
    }

    protected TextView createTextView() {
        var textView = new TextView();

        textView.setText(label);
        onTextCreatedListener.accept(textView);
        handleTextCreated(textView);
        textView.setId(LABEL_ID);
        textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        textView.setGravity(Gravity.CENTER);

        ParamsBuilder.using(RelativeLayout.LayoutParams::new)
                .heightMatchParent()
                .widthWrapContent()
                .setup(p -> {
                    p.addRule(ALIGN_TOP, EDIT_TEXT_ID);
                    p.addRule(ALIGN_PARENT_LEFT);
                    p.addRule(ALIGN_BOTTOM, EDIT_TEXT_ID);
                    p.rightMargin = dp(10);
                })
                .applyTo(textView);

        return textView;
    }

    protected EditText createEditText() {
        editText = new MaterialEditText();
        editText.setId(EDIT_TEXT_ID);

        onEditTextCreatedListener.accept(editText);

        ParamsBuilder.using(RelativeLayout.LayoutParams::new)
                .wrapContent()
                .setup(p -> {
                    p.addRule(ALIGN_PARENT_TOP);
                    p.addRule(END_OF, LABEL_ID);
                })
                .applyTo(editText);

        handleTextCreated(editText);

        return editText;
    }

    protected TextView createErrorTextView() {
        errorMessage = new TextView();

        onTextCreatedListener.accept(errorMessage);
        handleTextCreated(errorMessage);
        errorMessage.setTextSize(sp(17));
        errorMessage.setTextColor(Colors.RED);

        ParamsBuilder.using(RelativeLayout.LayoutParams::new)
                .widthMatchParent()
                .heightWrapContent()
                .setup(p -> {
                    p.addRule(BELOW, EDIT_TEXT_ID);
                    p.addRule(ALIGN_START, EDIT_TEXT_ID);
                    p.addRule(ALIGN_END, EDIT_TEXT_ID);
                })
                .applyTo(errorMessage);

        return errorMessage;
    }

    private void handleTextCreated(TextView textView) {
        textView.setTextSize(sp(20));
        textView.setTypeface(MINECRAFT_FONT);
    }

}
