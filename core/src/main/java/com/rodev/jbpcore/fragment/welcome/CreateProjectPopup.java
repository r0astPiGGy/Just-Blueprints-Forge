package com.rodev.jbpcore.fragment.welcome;

import com.rodev.jbpcore.utils.ColoredBackground;
import icyllis.modernui.graphics.Color;
import icyllis.modernui.view.*;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.PopupWindow;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static com.rodev.jbpcore.utils.ViewUtils.matchParent;

@RequiredArgsConstructor
public class CreateProjectPopup implements PopupWindow.OnDismissListener {

    private final PopupWindow mPopup = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private final View anchorView;
    private final View.OnAttachStateChangeListener mAttachStateChangeListener = new OnAttachStateChangeListener();
    private boolean mWasDismissed;

    @Setter
    private Consumer<CreateProjectPromptView> onPromptCreated = v -> {};

    private View createRootView() {
        var popupRoot = new FrameLayout() {
            @Override
            public boolean onTouchEvent(@NotNull MotionEvent event) {
                return true;
            }
        };

        ColoredBackground.of(Color.argb(120, 0, 0, 0)).applyTo(popupRoot);
        matchParent(popupRoot);

        popupRoot.addView(createPromptView());

        return popupRoot;
    }

    private CreateProjectPromptView createPromptView() {
        var prompt = new CreateProjectPromptView();

        prompt.setOnDeclineListener(this::dismiss);
        onPromptCreated.accept(prompt);

        return prompt;
    }

    public void show() {
        if(isShowing()) {
            throw new IllegalStateException("Already showing");
        }

        mPopup.setOnDismissListener(this);
        mPopup.setFocusable(true);

        anchorView.addOnAttachStateChangeListener(mAttachStateChangeListener);

        mPopup.setContentView(createRootView());
        mPopup.setIsClippedToScreen(true);
        mPopup.setOutsideTouchable(true);
        mPopup.setOverlapAnchor(true);
        mPopup.setTouchModal(true);
        mPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        mPopup.showAsDropDown(anchorView, 0, 0, Gravity.NO_GRAVITY);
        mPopup.getContentView().restoreDefaultFocus();
    }

    public void dismiss() {
        if (isShowing()) {
            mPopup.dismiss();
        }
    }

    public void onDismiss() {
        mWasDismissed = true;
        anchorView.removeOnAttachStateChangeListener(mAttachStateChangeListener);
    }

    public boolean isShowing() {
        return !mWasDismissed && mPopup.isShowing();
    }

    private class OnAttachStateChangeListener implements View.OnAttachStateChangeListener {
        @Override
        public void onViewAttachedToWindow(View v) {}

        @Override
        public void onViewDetachedFromWindow(View v) {
            dismiss();
        }
    }

}
