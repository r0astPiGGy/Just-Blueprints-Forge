package com.rodev.test.fragment.welcome;

import com.rodev.test.Colors;
import com.rodev.test.utils.ColoredBackground;
import icyllis.modernui.graphics.Color;
import icyllis.modernui.view.*;
import icyllis.modernui.widget.FrameLayout;
import icyllis.modernui.widget.PopupWindow;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import static com.rodev.test.utils.ViewUtils.matchParent;

@RequiredArgsConstructor
public class CreateProjectPopup {

    private final PopupWindow mPopup = new PopupWindow();
    private final View anchorView;

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
        return new CreateProjectPromptView();
    }

    public void show() {
        mPopup.setContentView(createRootView());
        mPopup.setOutsideTouchable(true);
        mPopup.setFocusable(true);
        mPopup.setTouchModal(true);
        mPopup.setIsClippedToScreen(true);
        mPopup.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopup.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        mPopup.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }

}
