/*
 * Modern UI.
 * Copyright (C) 2019-2022 BloCamLimb. All rights reserved.
 *
 * Modern UI is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Modern UI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Modern UI. If not, see <https://www.gnu.org/licenses/>.
 */

package com.rodev.test.contextmenu;

import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.*;
import icyllis.modernui.view.menu.ShowableListMenu;
import icyllis.modernui.widget.*;

import javax.annotation.Nullable;
import java.util.function.Supplier;

import static icyllis.modernui.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * A ListPopupWindow anchors itself to a host view and displays a
 * list of choices.
 *
 * <p>ListPopupWindow contains a number of tricky behaviors surrounding
 * positioning, scrolling parents to fit the dropdown, interacting
 * sanely with the IME if present, and others.
 *
 * @see Spinner
 */
public class BPContextMenu implements ShowableListMenu {
    private int mDropDownHorizontalOffset;
    private int mDropDownVerticalOffset;
    private boolean mOverlapAnchor;

    private int mDropDownGravity = Gravity.NO_GRAVITY;

    private View POPUP_ORIGIN_VIEW__REWORK;

    /**
     * Optional anchor-relative bounds to be used as the transition epicenter.
     * When {@code null}, the anchor bounds are used as the epicenter.
     */
    private Rect mEpicenterBounds;

    private boolean mModal;

    PopupWindow mPopup;

    private final int POPUP_WIDTH____REWORK;// = WRAP_CONTENT;
    private final int POPUP_HEIGHT____REWORK;// = WRAP_CONTENT;

    private final Supplier<View> viewFiller;

    public BPContextMenu(int width, int height, Supplier<View> viewFiller) {
        this.viewFiller = viewFiller;
        this.POPUP_WIDTH____REWORK = width;
        this.POPUP_HEIGHT____REWORK = height;

        mPopup = new PopupWindow(POPUP_WIDTH____REWORK, POPUP_HEIGHT____REWORK);
    }

    /**
     * Set whether this window should be modal when shown.
     *
     * <p>If a popup window is modal, it will receive all touch and key input.
     * If the user touches outside the popup window's content area the popup window
     * will be dismissed.
     *
     * @param modal {@code true} if the popup window should be modal, {@code false} otherwise.
     */
    public void setModal(boolean modal) {
        mModal = modal;
        mPopup.setFocusable(modal);
    }

    public boolean isModal() {
        return mModal;
    }

    /**
     * Sets a drawable to be the background for the popup window.
     *
     * @param d A drawable to set as the background.
     */
    public void setBackgroundDrawable(@Nullable Drawable d) {
        mPopup.setBackgroundDrawable(d);
    }

    /**
     * Sets the popup's anchor view. This popup will always be positioned relative to
     * the anchor view when shown.
     *
     * @param anchor The view to use as an anchor.
     */
    public void setAnchorView(@Nullable View anchor) {
        POPUP_ORIGIN_VIEW__REWORK = anchor;
    }
    /**
     * Set the horizontal offset of this popup from its anchor view in pixels.
     *
     * @param offset The horizontal offset of the popup from its anchor.
     */
    public void setHorizontalOffset(int offset) {
        mDropDownHorizontalOffset = offset;
    }

    /**
     * Set the vertical offset of this popup from its anchor view in pixels.
     *
     * @param offset The vertical offset of the popup from its anchor.
     */
    public void setVerticalOffset(int offset) {
        mDropDownVerticalOffset = offset;
    }

    public void setEpicenterBounds(@Nullable Rect bounds) {
        mEpicenterBounds = bounds != null ? bounds.copy() : null;
    }

    /**
     * Set the gravity of the dropdown list. This is commonly used to
     * set gravity to START or END for alignment with the anchor.
     *
     * @param gravity Gravity value to use
     */
    public void setDropDownGravity(int gravity) {
        mDropDownGravity = gravity;
    }

    /**
     * Show the popup list. If the list is already showing, this method
     * will recalculate the popup's size and position.
     */
    @Override
    public void show() {
        mPopup.setContentView(viewFiller.get());

        if (mPopup.isShowing()) {
            if (!POPUP_ORIGIN_VIEW__REWORK.isAttachedToWindow()) {
                //Don't update position if the anchor view is detached from window.
                return;
            }
            mPopup.setOutsideTouchable(true);
            mPopup.update(
                    POPUP_ORIGIN_VIEW__REWORK,
                    mDropDownHorizontalOffset,
                    mDropDownVerticalOffset,
                    POPUP_WIDTH____REWORK,
                    POPUP_HEIGHT____REWORK
            );
            mPopup.setOverlapAnchor(mOverlapAnchor);
            mPopup.getContentView().restoreDefaultFocus();
        } else {
            mPopup.setWidth(POPUP_WIDTH____REWORK);
            mPopup.setHeight(POPUP_HEIGHT____REWORK);
            mPopup.setIsClippedToScreen(true);

            // use outside touchable to dismiss drop down when touching outside of it, so
            // only set this if the dropdown is not always visible
            mPopup.setOutsideTouchable(true);
            mPopup.setEpicenterBounds(mEpicenterBounds);
            mPopup.setOverlapAnchor(mOverlapAnchor);
            mPopup.showAsDropDown(POPUP_ORIGIN_VIEW__REWORK, mDropDownHorizontalOffset,
                    mDropDownVerticalOffset, mDropDownGravity);
            mPopup.getContentView().restoreDefaultFocus();
        }
    }

    /**
     * Dismiss the popup window.
     */
    @Override
    public void dismiss() {
        mPopup.dismiss();
        mPopup.setContentView(null);
    }

    /**
     * Remove existing exit transition from PopupWindow and force immediate dismissal.
     *
     * @hide
     */
    public void dismissImmediate() {
        mPopup.setExitTransition(null);
        dismiss();
    }

    /**
     * @return {@code true} if the popup is currently showing, {@code false} otherwise.
     */
    @Override
    public boolean isShowing() {
        return mPopup.isShowing();
    }

    /**
     * @return The {@link ListView} displayed within the popup window.
     * Only valid when {@link #isShowing()} == {@code true}.
     */
    @Nullable
    @Override
    public ListView getListView() {
        throw new IllegalStateException();
    }

    public void setOverlapAnchor(boolean overlap) {
        mOverlapAnchor = overlap;
    }

}
