package com.rodev.test.contextmenu;

import icyllis.modernui.math.Rect;
import icyllis.modernui.view.Gravity;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewConfiguration;
import icyllis.modernui.view.menu.*;
import icyllis.modernui.widget.PopupMenu;
import icyllis.modernui.widget.PopupWindow;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BPMenuPopupHelper implements MenuHelper {

    // Mutable cached popup menu properties.
    private View mAnchorView;
    private int mDropDownGravity = Gravity.START;
    private MenuPresenter.Callback mPresenterCallback;

    private MenuPopup mPopup;
    private PopupWindow.OnDismissListener mOnDismissListener;

    /**
     * Listener used to proxy dismiss callbacks to the helper's owner.
     */
    private final PopupWindow.OnDismissListener mInternalOnDismissListener = this::onDismiss;

    public BPMenuPopupHelper(View anchorView) {
        mAnchorView = anchorView;
    }

    public void setOnDismissListener(@Nullable PopupWindow.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    /**
     * Sets the alignment of the popup window relative to the anchor view.
     * <p>
     * Changes take effect on the next call to show().
     *
     * @param gravity alignment of the popup relative to the anchor
     */
    public void setGravity(int gravity) {
        mDropDownGravity = gravity;
    }

    /**
     * @return alignment of the popup relative to the anchor
     */
    public int getGravity() {
        return mDropDownGravity;
    }

    public void show(int x, int y) {
        if (!tryShow(x, y)) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }

    @Nonnull
    public MenuPopup getPopup() {
        if (mPopup == null) {
            mPopup = createPopup();
        }
        return mPopup;
    }

    /**
     * Shows the popup menu and makes a best-effort to anchor it to the
     * specified (x,y) coordinate relative to the anchor view.
     * <p>
     * Additionally, the popup's transition epicenter (see
     * {@link PopupWindow#setEpicenterBounds(Rect)} will be
     * centered on the specified coordinate, rather than using the bounds of
     * the anchor view.
     * <p>
     * If the popup's resolved gravity is {@link Gravity#LEFT}, this will
     * display the popup with its top-left corner at (x,y) relative to the
     * anchor view. If the resolved gravity is {@link Gravity#RIGHT}, the
     * popup's top-right corner will be at (x,y).
     * <p>
     * If the popup cannot be displayed fully on-screen, this method will
     * attempt to scroll the anchor view's ancestors and/or offset the popup
     * such that it may be displayed fully on-screen.
     *
     * @param x x coordinate relative to the anchor view
     * @param y y coordinate relative to the anchor view
     * @return {@code true} if the popup was shown or was already showing prior
     * to calling this method, {@code false} otherwise
     */
    public boolean tryShow(int x, int y) {
        if (isShowing()) {
            return true;
        }

        if (mAnchorView == null) {
            return false;
        }

        showPopup(x, y, true, true);
        return true;
    }

    /**
     * Creates the popup and assigns cached properties.
     *
     * @return an initialized popup
     */
    @Nonnull
    private MenuPopup createPopup() {
        final MenuPopup popup = new BlueprintMenuPopup(mAnchorView);

        popup.setOnDismissListener(mInternalOnDismissListener);

        // Assign mutable properties. These may be reassigned later.
        popup.setAnchorView(mAnchorView);
        popup.setCallback(mPresenterCallback);
        popup.setGravity(mDropDownGravity);

        return popup;
    }

    private void showPopup(int xOffset, int yOffset, boolean useOffsets, boolean showTitle) {
        final MenuPopup popup = getPopup();
        popup.setShowTitle(showTitle);

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

    /**
     * Called after the popup has been dismissed.
     * <p>
     * <strong>Note:</strong> Subclasses should call the super implementation
     * last to ensure that any necessary tear down has occurred before the
     * listener specified by {@link #setOnDismissListener(PopupWindow.OnDismissListener)}
     * is called.
     */
    protected void onDismiss() {
        mPopup = null;

        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
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
