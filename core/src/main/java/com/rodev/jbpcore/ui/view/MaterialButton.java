package com.rodev.jbpcore.ui.view;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.ui.drawable.MaterialButtonBackground;
import com.rodev.jbpcore.utils.ParamsBuilder;
import icyllis.modernui.core.Context;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.util.StateSet;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.PointerIcon;
import icyllis.modernui.widget.LinearLayout;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.TextView;

import javax.annotation.Nonnull;

import static com.rodev.jbpcore.Fonts.MINECRAFT_FONT;

public class MaterialButton extends RelativeLayout {

    private static final ColorStateList TINT_LIST = createColorStateList(Colors.BUTTON_COLOR_PRIMARY);

    public static ColorStateList createColorStateList(int buttonColor) {
        return new ColorStateList(
                new int[][]{
                        StateSet.get(StateSet.VIEW_STATE_ENABLED),
                        StateSet.WILD_CARD},
                new int[]{
                        buttonColor,
                        Colors.BUTTON_DISABLED}
        );
    }

    private final TextView textView;

    protected final LinearLayout container;

    private MaterialButton(Context context, ColorStateList stateList) {
        super(context);

        textView = new TextView(context);
        container = new LinearLayout(context);

        var background = new MaterialButtonBackground(this::dp, dp(5));
        background.setTintList(stateList);
        setBackground(background);
        setMinimumWidth(dp(140));

        initTextView();

        container.setOrientation(LinearLayout.HORIZONTAL);

        ParamsBuilder.using(RelativeLayout.LayoutParams::new)
                .wrapContent()
                .setup(p -> {
                    p.addRule(CENTER_HORIZONTAL);
                    p.addRule(ALIGN_PARENT_TOP);
                })
                .applyTo(container);

        container.addView(textView);

        addView(container);
    }

    private void initTextView() {
        textView.setTypeface(MINECRAFT_FONT);
        textView.setTextSize(sp(20));
        textView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        ParamsBuilder.using(LinearLayout.LayoutParams::new)
                .wrapContent()
                .applyTo(textView);
    }

    public MaterialButton(Context context, int color) {
        this(context, createColorStateList(color));
    }

    public MaterialButton(Context context) {
        this(context, TINT_LIST);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if(enabled) {
            textView.setTextColor(Colors.WHITE);
        } else {
            textView.setTextColor(Colors.BUTTON_TEXT_DISABLED);
        }
    }

    @Override
    public PointerIcon onResolvePointerIcon(@Nonnull MotionEvent event) {
        if (isClickable() && isEnabled()) {
            return PointerIcon.getSystemIcon(PointerIcon.TYPE_HAND);
        }
        return super.onResolvePointerIcon(event);
    }

    public TextView getTextView() {
        return textView;
    }

    public void setText(String text) {
        getTextView().setText(text);
    }
}
