package com.rodev.test.contextmenu;

import com.rodev.test.blueprint.data.action.Action;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewConfiguration;
import icyllis.modernui.view.menu.*;
import icyllis.modernui.widget.PopupWindow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BPMenuPopupHelper implements MenuHelper {

    // Mutable cached popup menu properties.
    private final View mAnchorView;
    private int mDropDownGravity = Gravity.START;
    private MenuPresenter.Callback mPresenterCallback;

    private BlueprintMenuPopup mPopup;

    /**
     * Listener used to proxy dismiss callbacks to the helper's owner.
     */
    private final PopupWindow.OnDismissListener mInternalOnDismissListener = this::onDismiss;

    public BPMenuPopupHelper(View anchorView) {
        mAnchorView = anchorView;
    }

    public void show(ContextMenuBuilder builder) {
        if (!tryShow(builder)) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }

    @Nonnull
    public BlueprintMenuPopup getPopup(ContextMenuBuilder builder) {
        if (mPopup == null) {
            mPopup = createPopup(builder);
        }
        return mPopup;
    }


    public boolean tryShow(ContextMenuBuilder builder) {
        if (isShowing()) {
            return true;
        }

        if (mAnchorView == null) {
            return false;
        }

        showPopup(builder, true, true);
        return true;
    }

    /**
     * Creates the popup and assigns cached properties.
     *
     * @return an initialized popup
     */
    @Nonnull
    private BlueprintMenuPopup createPopup(ContextMenuBuilder contextMenuBuilder) {
        final BlueprintMenuPopup popup = new BlueprintMenuPopup(contextMenuBuilder, mAnchorView);

        popup.setOnDismissListener(mInternalOnDismissListener);

        // Assign mutable properties. These may be reassigned later.
        popup.setAnchorView(mAnchorView);
        popup.setCallback(mPresenterCallback);
        popup.setGravity(mDropDownGravity);

        return popup;
    }

    private void showPopup(ContextMenuBuilder builder, boolean useOffsets, boolean showTitle) {
        final BlueprintMenuPopup popup = getPopup(builder);
        popup.setShowTitle(showTitle);

        int xOffset = builder.x;
        int yOffset = builder.y;

        if (useOffsets) {
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
            final Rect epicenter = new Rect(xOffset - halfSize, yOffset - halfSize,
                    xOffset + halfSize, yOffset + halfSize);
            popup.setEpicenterBounds(epicenter);
        }

        popup.show();
    }

    /**
     * Dismisses the popup, if showing.
     */
    @Override
    public void dismiss() {
        if (isShowing()) {
            mPopup.dismiss();
        }
    }

    protected void onDismiss() {
        mPopup = null;
    }

    public boolean isShowing() {
        return mPopup != null && mPopup.isShowing();
    }

    @Override
    public void setPresenterCallback(@Nullable MenuPresenter.Callback cb) {
        mPresenterCallback = cb;
        if (mPopup != null) {
            mPopup.setCallback(cb);
        }
    }
}
