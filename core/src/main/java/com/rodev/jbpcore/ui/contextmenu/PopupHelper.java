package com.rodev.jbpcore.ui.contextmenu;

import icyllis.modernui.math.Rect;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewConfiguration;
import icyllis.modernui.widget.PopupWindow;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

@RequiredArgsConstructor
public class PopupHelper {

    private final View mAnchorView;
    private final ContextMenuBuilder builder;

    private final int mDropDownGravity = Gravity.START;
    private BlueprintPopup mPopup;

    private final PopupWindow.OnDismissListener mInternalOnDismissListener = this::onDismiss;

    public static void show(View anchorView, ContextMenuBuilder builder) {
        new PopupHelper(anchorView, builder).show();
    }

    public void show() {
        if (mAnchorView == null) {
            throw new IllegalStateException("PopupHelper cannot be used without an anchor");
        }

        tryShow();
    }

    public void tryShow() {
        if (isShowing()) {
            return;
        }

        showPopup();
    }

    @Nonnull
    public BlueprintPopup getPopup() {
        if (mPopup == null) {
            mPopup = createPopup();
        }
        return mPopup;
    }

    /**
     * Creates the popup and assigns cached properties.
     *
     * @return an initialized popup
     */
    @Nonnull
    private BlueprintPopup createPopup() {
        final BlueprintPopup popup = new BlueprintPopup(builder, mAnchorView);

        popup.setOnDismissListener(mInternalOnDismissListener);

        // Assign mutable properties. These may be reassigned later.
        popup.setAnchorView(mAnchorView);
        popup.setGravity(mDropDownGravity);

        return popup;
    }

    private void showPopup() {
        final BlueprintPopup popup = getPopup();

        int xOffset = builder.x;
        int yOffset = builder.y;

        // If the resolved drop-down gravity is RIGHT, the popup's right
        // edge will be aligned with the anchor view. Adjust by the anchor
        // width such that the top-right corner is at the X offset.
        final int hgrav = Gravity.getAbsoluteGravity(mDropDownGravity,
                mAnchorView.getLayoutDirection()) & Gravity.HORIZONTAL_GRAVITY_MASK;
        if (hgrav == Gravity.RIGHT) {
            xOffset -= mAnchorView.getWidth();
        }

        popup.setHorizontalOffset(xOffset);
        popup.setVerticalOffset(yOffset);

        // Set the transition epicenter to be roughly finger (or mouse
        // cursor) sized and centered around the offset position. This
        // will give the appearance that the window is emerging from
        // the touch point.
        final int halfSize = (int) (24 * ViewConfiguration.get().getViewScale());
        final Rect epicenter = new Rect(
                xOffset - halfSize,
                yOffset - halfSize,
                xOffset + halfSize,
                yOffset + halfSize
        );
        popup.setEpicenterBounds(epicenter);

        popup.show();
    }

    protected void onDismiss() {
        mPopup = null;
    }

    public boolean isShowing() {
        return mPopup != null && mPopup.isShowing();
    }

}
