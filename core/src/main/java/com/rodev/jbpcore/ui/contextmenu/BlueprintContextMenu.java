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

package com.rodev.jbpcore.ui.contextmenu;

import com.rodev.jbpcore.Colors;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.view.*;
import icyllis.modernui.widget.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public class BlueprintContextMenu {
    private int mDropDownHorizontalOffset;
    private int mDropDownVerticalOffset;
    private final boolean mOverlapAnchor = true;

    private int mDropDownGravity = Gravity.NO_GRAVITY;

    private View anchorView;

    /**
     * Optional anchor-relative bounds to be used as the transition epicenter.
     * When {@code null}, the anchor bounds are used as the epicenter.
     */
    private Rect mEpicenterBounds;

    private boolean mModal;

    PopupWindow mPopup;

    private final int width;
    private final int height;

    private final Supplier<View> viewFiller;

    public BlueprintContextMenu(int width, int height, Supplier<View> viewFiller) {
        this.viewFiller = viewFiller;
        this.width = width;
        this.height = height;

        mPopup = new PopupWindow(this.width, this.height);

        setBackgroundDrawable(new Background());
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

    public void setDismissListener(PopupWindow.OnDismissListener listener) {
        mPopup.setOnDismissListener(listener);
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
        anchorView = anchor;
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

    public void show() {
        mPopup.setContentView(viewFiller.get());

        if (mPopup.isShowing()) {
            if (!anchorView.isAttachedToWindow()) {
                //Don't update position if the anchor view is detached from window.
                return;
            }
            mPopup.setOutsideTouchable(true);
            mPopup.update(
                    anchorView,
                    mDropDownHorizontalOffset,
                    mDropDownVerticalOffset,
                    width,
                    height
            );
            mPopup.setOverlapAnchor(mOverlapAnchor);
            mPopup.getContentView().restoreDefaultFocus();
        } else {
            mPopup.setWidth(width);
            mPopup.setHeight(height);
            mPopup.setIsClippedToScreen(true);
            mPopup.setOutsideTouchable(true);
            mPopup.setEpicenterBounds(mEpicenterBounds);
            mPopup.setOverlapAnchor(mOverlapAnchor);
            mPopup.showAsDropDown(anchorView, mDropDownHorizontalOffset,
                    mDropDownVerticalOffset, mDropDownGravity);
            mPopup.getContentView().restoreDefaultFocus();
        }
    }

    public void dismiss() {
        mPopup.dismiss();
        mPopup.setContentView(null);
    }

    public boolean isShowing() {
        return mPopup.isShowing();
    }

    private static class Background extends Drawable {

        @Override
        public void draw(@Nonnull Canvas canvas) {
            Paint paint = Paint.obtain();
            paint.setColor(Colors.NODE_BACKGROUND);
            Rect b = getBounds();
            canvas.drawRect(b.left, b.top, b.right, b.bottom, paint);
        }

        @Override
        public boolean getPadding(@Nonnull Rect padding) {
            int r = (int) (double) 8;
            padding.set(r, r, r, r);
            return true;
        }
    }
}
