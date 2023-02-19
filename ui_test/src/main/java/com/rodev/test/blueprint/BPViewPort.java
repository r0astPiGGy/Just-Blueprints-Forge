/*
 * Modern UI.
 * Copyright (C) 2019-2021 BloCamLimb. All rights reserved.
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

package com.rodev.test.blueprint;

import com.rodev.test.contextmenu.BPContextMenuBuilder;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.*;
import icyllis.modernui.view.menu.ContextMenuBuilder;
import icyllis.modernui.view.menu.MenuHelper;
import icyllis.modernui.widget.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;

public class BPViewPort extends FrameLayout {

    private final Rect mTempRect = new Rect();

    private final OverScroller mScroller;

    /**
     * Tracks the state of the top edge glow.
     * <p>
     * Even though this field is practically final, we cannot make it final because there are apps
     * setting it via reflection and they need to keep working until they target Q.
     */
    private final EdgeEffect mEdgeGlowTop;

    /**
     * Tracks the state of the bottom edge glow.
     * <p>
     * Even though this field is practically final, we cannot make it final because there are apps
     * setting it via reflection and they need to keep working until they target Q.
     */
    private final EdgeEffect mEdgeGlowBottom;

    /**
     * Position of the last motion event.
     */
    private int mLastMotionY;
    private int mLastMotionX;

    /**
     * True when the layout has changed but the traversal has not come through yet.
     * Ideally the view hierarchy would keep track of this for us.
     */
    private boolean mIsLayoutDirty = true;

    /**
     * The child to give focus to in the event that a child has requested focus while the
     * layout is dirty. This prevents the scroll from being wrong if the child has not been
     * laid out before requesting focus.
     */
    private View mChildToScrollTo = null;

    /**
     * True if the user is currently dragging this ScrollView around. This is
     * not the same as 'is being flinged', which can be checked by
     * mScroller.isFinished() (flinging begins when the user lifts their finger).
     */
    private boolean mIsBeingDragged = false;

    /**
     * Determines speed during touch scrolling
     */
    private VelocityTracker mVelocityTracker;

    private final int mTouchSlop;
    private final int mMinimumVelocity = 0;
    private final int mMaximumVelocity = 0;

    private final int mOverscrollDistanceY = 0;
    private final int mOverscrollDistanceX = 0;
    private final int mOverflingDistanceY = 0;
    private final int mOverflingDistanceX = 0;

    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mNestedYOffset;
    private int mNestedXOffset;
    private boolean notCentered = true;

    public BPViewPort() {
        mScroller = new OverScroller();
        mEdgeGlowTop = new EdgeEffect();
        mEdgeGlowBottom = new EdgeEffect();
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final ViewConfiguration configuration = ViewConfiguration.get();

        mTouchSlop = configuration.getScaledTouchSlop();

        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    BPContextMenuBuilder mContextMenu;
    MenuHelper mContextMenuHelper;

    @Override
    public boolean showContextMenuForChild(View originalView, float x, float y) {
        if (mContextMenuHelper != null) {
            mContextMenuHelper.dismiss();
            mContextMenuHelper = null;
        }

        if (mContextMenu == null) {
            mContextMenu = new BPContextMenuBuilder();
        } else {
            mContextMenu.clearAll();
        }

        final MenuHelper helper;
        final boolean isPopup = !Float.isNaN(x) && !Float.isNaN(y);
        if (isPopup) {
            helper = mContextMenu.showPopup(originalView, x, y);
        } else {
            helper = mContextMenu.showPopup(originalView, 0, 0);
        }

        mContextMenuHelper = helper;
        return helper != null;
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return true;
    }

    @Override
    public void addView(@Nonnull View child) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child);
    }

    @Override
    public void addView(@Nonnull View child, int index) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child, index);
    }

    @Override
    public void addView(@Nonnull View child, @Nonnull ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child, params);
    }

    @Override
    public void addView(@Nonnull View child, int index, @Nonnull ViewGroup.LayoutParams params) {
        if (getChildCount() > 0) {
            throw new IllegalStateException("ScrollView can host only one direct child");
        }
        super.addView(child, index, params);
    }
    private boolean inChild(int x, int y) {
        if (getChildCount() > 0) {
            final int scrollY = mScrollY;
            final int scrollX = mScrollX;
            final View child = getChildAt(0);
            return !(y < child.getTop() - scrollY
                    || y >= child.getBottom() - scrollY
                    || x < child.getLeft() - scrollX
                    || x >= child.getRight() - scrollX);
        }
        return false;
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean onInterceptTouchEvent(@Nonnull MotionEvent ev) {

        if(ev.getButtonState() != MotionEvent.BUTTON_TERTIARY) return false;
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onMotionEvent will be called and we do the actual
         * scrolling there.
         */

        /*
         * Shortcut the most recurring case: the user is in the dragging
         * state and they is moving their finger.  We want to intercept this
         * motion.
         */
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }

        if (super.onInterceptTouchEvent(ev)) {
            return true;
        }

        /*
         * Don't try to intercept touch if we can't scroll anyway.
         */
        if ((getScrollY() == 0 && !canScrollVertically(1)) ||
            (getScrollX() == 0 && !canScrollHorizontally(1))
        ) {
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE -> {
                final int y = (int) ev.getY();
                final int x = (int) ev.getX();
                final int yDiff = Math.abs(y - mLastMotionY);
                final int xDiff = Math.abs(x - mLastMotionX);
                if ((yDiff > mTouchSlop && (getNestedScrollAxes() & SCROLL_AXIS_VERTICAL) == 0) ||
                        (xDiff > mTouchSlop && (getNestedScrollAxes() & SCROLL_AXIS_HORIZONTAL) == 0)
                ) {
                    mIsBeingDragged = true;
                    mLastMotionY = y;
                    mLastMotionX = x;
                    initVelocityTrackerIfNotExists();
                    mVelocityTracker.addMovement(ev);
                    mNestedYOffset = 0;
                    mNestedXOffset = 0;
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
            }
            case MotionEvent.ACTION_DOWN -> {
                final int y = (int) ev.getY();
                final int x = (int) ev.getX();

                if (!inChild(x, y)) {
                    mIsBeingDragged = false;
                    recycleVelocityTracker();
                    break;
                }

                /*
                 * Remember location of down touch.
                 * ACTION_DOWN always refers to pointer index 0.
                 */
                mLastMotionY = y;
                mLastMotionX = x;

                initOrResetVelocityTracker();
                mVelocityTracker.addMovement(ev);
                /*
                 * If being flinged and user touches the screen, initiate drag;
                 * otherwise don't. mScroller.isFinished should be false when
                 * being flinged. We need to call computeScrollOffset() first so that
                 * isFinished() is correct.
                 */
                mScroller.computeScrollOffset();
                mIsBeingDragged = !mScroller.isFinished() || !mEdgeGlowBottom.isFinished()
                        || !mEdgeGlowTop.isFinished();
                // Catch the edge effect if it is active.
                if (!mEdgeGlowTop.isFinished()) {
                    mEdgeGlowTop.onPullDistance(0f, ev.getX() / getWidth());
                }
                if (!mEdgeGlowBottom.isFinished()) {
                    mEdgeGlowBottom.onPullDistance(0f, 1f - ev.getX() / getWidth());
                }
                startNestedScroll(SCROLL_AXIS_VERTICAL | SCROLL_AXIS_HORIZONTAL, TYPE_TOUCH);
            }
            case MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                /* Release the drag */
                mIsBeingDragged = false;
                recycleVelocityTracker();
                if (mScroller.springBack(mScrollX, mScrollY, 0, getXScrollRange(), 0, getYScrollRange())) {
                    postInvalidateOnAnimation();
                }
                stopNestedScroll(TYPE_TOUCH);
            }
        }

        /*
         * The only time we want to intercept motion events is if we are in the
         * drag mode.
         */
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(@Nonnull MotionEvent ev) {
        if(ev.getButtonState() != MotionEvent.BUTTON_TERTIARY) return false;

        initVelocityTrackerIfNotExists();

        MotionEvent vtev = ev.copy();

        final int action = ev.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
            mNestedYOffset = 0;
            mNestedXOffset = 0;
        }
        vtev.offsetLocation(mNestedXOffset, mNestedYOffset);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                if (getChildCount() == 0) {
                    vtev.recycle();
                    return false;
                }
                if (!mScroller.isFinished()) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }

                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.
                 */
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                // Remember where the motion event started
                mLastMotionY = (int) ev.getY();
                mLastMotionX = (int) ev.getX();
                startNestedScroll(SCROLL_AXIS_VERTICAL | SCROLL_AXIS_HORIZONTAL, TYPE_TOUCH);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final int x = (int) ev.getX();
                final int y = (int) ev.getY();
                int deltaX = mLastMotionX - x;
                int deltaY = mLastMotionY - y;

                if (dispatchNestedPreScroll(deltaX, deltaY, mScrollConsumed, mScrollOffset, TYPE_TOUCH)) {
                    deltaX -= mScrollConsumed[0];
                    deltaY -= mScrollConsumed[1];

                    vtev.offsetLocation(mScrollOffset[0], mScrollOffset[1]);
                    mNestedXOffset += mScrollOffset[0];
                    mNestedYOffset += mScrollOffset[1];
                }
                if (!mIsBeingDragged && (Math.abs(deltaY) > mTouchSlop || Math.abs(deltaX) > mTouchSlop) ) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    mIsBeingDragged = true;
                    if (deltaY > 0) {
                        deltaY -= mTouchSlop;
                    } else {
                        deltaY += mTouchSlop;
                    }
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                    mLastMotionX = x - mScrollOffset[0];
                    mLastMotionY = y - mScrollOffset[1];

                    final int oldX = mScrollX;
                    final int oldY = mScrollY;
                    final int xRange = getXScrollRange();
                    final int yRange = getYScrollRange();

                    // Calling overScrollBy will call onOverScrolled, which
                    // calls onScrollChanged if applicable.
                    if (overScrollBy(deltaX, deltaY, mScrollX, mScrollY, xRange, yRange, mOverscrollDistanceX, mOverscrollDistanceY, true)
                            && !hasNestedScrollingParent(TYPE_TOUCH)) {
                        // Break our velocity if we hit a scroll barrier.
                        mVelocityTracker.clear();
                    }

                    final int scrolledDeltaY = mScrollY - oldY;
                    final int scrolledDeltaX = mScrollX - oldX;
                    final int unconsumedY = deltaY - scrolledDeltaY;
                    final int unconsumedX = deltaX - scrolledDeltaX;
                    if (dispatchNestedScroll(scrolledDeltaX, scrolledDeltaY, unconsumedX, unconsumedY, mScrollOffset, TYPE_TOUCH,
                            mScrollConsumed)) {
                        mLastMotionX -= mScrollOffset[0];
                        mLastMotionY -= mScrollOffset[1];
                        vtev.offsetLocation(mScrollOffset[0], mScrollOffset[1]);
                        mNestedXOffset += mScrollOffset[0];
                        mNestedYOffset += mScrollOffset[1];
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialVelocity = (int) velocityTracker.getYVelocity();

                    if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                        throw new IllegalStateException("Unknown method");
                        //flingWithNestedDispatch(-initialVelocity);
                    } else if (mScroller.springBack(mScrollX, mScrollY, 0, getXScrollRange(), 0,
                            getYScrollRange())) {
                        postInvalidateOnAnimation();
                    }

                    endDrag();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged && getChildCount() > 0) {
                    if (mScroller.springBack(mScrollX, mScrollY, 0, getXScrollRange(), 0, getYScrollRange())) {
                        postInvalidateOnAnimation();
                    }
                    endDrag();
                }
                break;
        }

        if (mVelocityTracker != null) {
            mVelocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

    @Override
    public boolean onGenericMotionEvent(@Nonnull MotionEvent event) {
        // TODO: Handle zoom
//        if (event.getAction() == MotionEvent.ACTION_SCROLL) {
//
//        }
        return super.onGenericMotionEvent(event);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        // Treat animating scrolls differently; see #computeScroll() for why.
        if (!mScroller.isFinished()) {
            final int oldX = mScrollX;
            final int oldY = mScrollY;
            mScrollX = scrollX;
            mScrollY = scrollY;
            onScrollChanged(mScrollX, mScrollY, oldX, oldY);
            if (clampedY) {
                mScroller.springBack(mScrollX, mScrollY, 0, getXScrollRange(), 0, getYScrollRange());
            }
        } else {
            super.scrollTo(scrollX, scrollY);
        }
    }

    private int getYScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0,
                    child.getHeight() - (getHeight() - mPaddingBottom - mPaddingTop));
        }
        return scrollRange;
    }

    private int getXScrollRange() {
        int scrollRange = 0;
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            scrollRange = Math.max(0,
                    child.getWidth() - (getWidth() - mPaddingRight - mPaddingLeft));
        }
        return scrollRange;
    }

    /**
     * @return whether the descendant of this scroll view is within delta
     * pixels of being on the screen.
     */
    private boolean isWithinDeltaOfScreen(@Nonnull View descendant, int delta, int height) {
        System.out.println("isWithinDeltaOfScreen is USED");
        descendant.getDrawingRect(mTempRect);
        offsetDescendantRectToMyCoords(descendant, mTempRect);

        return (mTempRect.bottom + delta) >= getScrollY()
                && (mTempRect.top - delta) <= (getScrollY() + height);
    }

    /**
     * Like {@link View#scrollBy}, but scroll smoothly instead of immediately.
     *
     * @param deltaY the number of pixels to scroll by on the Y axis
     * @return if actually scrolled
     */
    public final boolean smoothScrollBy(int deltaX, int deltaY) {
        System.out.println("smoothScrollBy is USED");
        if (getChildCount() == 0) {
            // Nothing to do.
            return false;
        }
        deltaX = Math.max(0, Math.min(mScroller.getFinalX() + deltaX, getXScrollRange())) - mScrollX;
        deltaY = Math.max(0, Math.min(mScroller.getFinalY() + deltaY, getYScrollRange())) - mScrollY;
        if (deltaX != 0 || deltaY != 0) {
            mScroller.startScroll(mScrollX, mScrollY, deltaX, deltaY);
            postInvalidateOnAnimation();
            return true;
        }
        return false;
    }

    /**
     * <p>The scroll range of a scroll view is the overall height of all of its
     * children.</p>
     */
    @Override
    protected int computeVerticalScrollRange() {
        final int count = getChildCount();
        final int contentHeight = getHeight() - mPaddingBottom - mPaddingTop;
        if (count == 0) {
            return contentHeight;
        }

        int scrollRange = getChildAt(0).getBottom();
        final int scrollY = mScrollY;
        final int overscrollBottom = Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom;
        }

        return scrollRange;
    }

    @Override
    protected int computeHorizontalScrollRange() {
        final int count = getChildCount();
        final int contentWidth = getWidth() - mPaddingRight - mPaddingLeft;
        if (count == 0) {
            return contentWidth;
        }

        int scrollRange = getChildAt(0).getRight();
        final int scrollX = mScrollX;
        final int overscrollRight = Math.max(0, scrollRange - contentWidth);
        if (scrollX < 0) {
            scrollRange -= scrollX;
        } else if (scrollX > overscrollRight) {
            scrollRange += scrollX - overscrollRight;
        }

        return scrollRange;
    }

    @Override
    protected int computeHorizontalScrollOffset() {
        return Math.max(0, super.computeHorizontalScrollOffset());
    }

    @Override
    protected int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    @Override
    protected void measureChild(@Nonnull View child, int parentWidthMeasureSpec,
                                int parentHeightMeasureSpec) {
        int childWidthMeasureSpec;
        int childHeightMeasureSpec;

        final int horizontalPadding = mPaddingRight + mPaddingLeft;
        childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - horizontalPadding),
                MeasureSpec.UNSPECIFIED);

        final int verticalPadding = mPaddingTop + mPaddingBottom;
        childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - verticalPadding),
                MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void measureChildWithMargins(@Nonnull View child, int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int usedTotalWidth = mPaddingLeft + mPaddingRight + lp.leftMargin + lp.rightMargin + widthUsed;
        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - usedTotalWidth),
                MeasureSpec.UNSPECIFIED);

        final int usedTotalHeight = mPaddingTop + mPaddingBottom + lp.topMargin + lp.bottomMargin + heightUsed;
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                Math.max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - usedTotalHeight),
                MeasureSpec.UNSPECIFIED);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            // This is called at drawing time by ViewGroup.  We don't want to
            // re-show the scrollbars at this point, which scrollTo will do,
            // so we replicate most of scrollTo here.
            //
            //         It's a little odd to call onScrollChanged from inside the drawing.
            //
            //         It is, except when you remember that computeScroll() is used to
            //         animate scrolling. So unless we want to defer the onScrollChanged()
            //         until the end of the animated scrolling, we don't really have a
            //         choice here.
            //
            //         I agree.  The alternative, which I think would be worse, is to post
            //         something and tell the subclasses later.  This is bad because there
            //         will be a window where mScrollX/Y is different from what the app
            //         thinks it is.
            //
            int oldX = mScrollX;
            int oldY = mScrollY;
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            if (oldX != x || oldY != y) {
                final int rangeX = getXScrollRange();
                final int rangeY = getYScrollRange();
                final int overscrollMode = getOverScrollMode();
                final boolean canOverscroll = overscrollMode == OVER_SCROLL_ALWAYS ||
                        (overscrollMode == OVER_SCROLL_IF_CONTENT_SCROLLS && rangeY > 0);

                overScrollBy(x - oldX, y - oldY, oldX, oldY, rangeX, rangeY,
                        mOverflingDistanceX, mOverflingDistanceY, false);
                onScrollChanged(mScrollX, mScrollY, oldX, oldY);

                if (canOverscroll) {
                    if (y < 0 && oldY >= 0) {
                        mEdgeGlowTop.onAbsorb((int) mScroller.getCurrVelocity());
                    } else if (y > rangeY && oldY <= rangeY) {
                        mEdgeGlowBottom.onAbsorb((int) mScroller.getCurrVelocity());
                    }
                }
            }

            if (!awakenScrollBars()) {
                // Keep on drawing until the animation has finished.
                postInvalidateOnAnimation();
            }
        }
    }

    /**
     * Scrolls the view to the given child.
     *
     * @param child the View to scroll to
     */
    public void scrollToDescendant(@Nonnull View child) {
        if (!mIsLayoutDirty) {
            child.getDrawingRect(mTempRect);

            /* Offset from child's local coordinates to ScrollView coordinates */
            offsetDescendantRectToMyCoords(child, mTempRect);

            int scrollDeltaX = computeScrollXDeltaToGetChildRectOnScreen(mTempRect);
            int scrollDeltaY = computeScrollYDeltaToGetChildRectOnScreen(mTempRect);

            if (scrollDeltaX != 0 || scrollDeltaY != 0) {
                scrollBy(scrollDeltaX, scrollDeltaY);
            }
        } else {
            mChildToScrollTo = child;
        }
    }

    /**
     * If rect is offscreen, scroll just enough to get it (or at least the
     * first screen size chunk of it) on screen.
     *
     * @param rect      The rectangle.
     * @param immediate True to scroll immediately without animation
     * @return true if scrolling was performed
     */
    private boolean scrollToChildRect(Rect rect, boolean immediate) {
        final int deltaX = computeScrollXDeltaToGetChildRectOnScreen(rect);
        final int deltaY = computeScrollYDeltaToGetChildRectOnScreen(rect);
        if (deltaX != 0 || deltaY != 0) {
            if (immediate) {
                scrollBy(deltaX, deltaY);
            } else {
                smoothScrollBy(deltaX, deltaY);
            }
            return true;
        }
        return false;
    }

    /**
     * Compute the amount to scroll in the Y direction in order to get
     * a rectangle completely on the screen (or, if taller than the screen,
     * at least the first screen size chunk of it).
     *
     * @param rect The rect.
     * @return The scroll delta.
     */
    protected int computeScrollYDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) return 0;

        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;

        int scrollYDelta = 0;

        if (rect.bottom > screenBottom && rect.top > screenTop) {
            // need to move down to get it in view: move down just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).

            if (rect.height() > height) {
                // just enough to get screen size chunk on
                scrollYDelta += (rect.top - screenTop);
            } else {
                // get entire rect at bottom of screen
                scrollYDelta += (rect.bottom - screenBottom);
            }

            // make sure we aren't scrolling beyond the end of our content
            int bottom = getChildAt(0).getBottom();
            int distanceToBottom = bottom - screenBottom;
            scrollYDelta = Math.min(scrollYDelta, distanceToBottom);

        } else if (rect.top < screenTop && rect.bottom < screenBottom) {
            // need to move up to get it in view: move up just enough so that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).

            if (rect.height() > height) {
                // screen size chunk
                scrollYDelta -= (screenBottom - rect.bottom);
            } else {
                // entire rect at top
                scrollYDelta -= (screenTop - rect.top);
            }

            // make sure we aren't scrolling any further than the top our content
            scrollYDelta = Math.max(scrollYDelta, -getScrollY());
        }
        return scrollYDelta;
    }

    protected int computeScrollXDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) return 0;

        int width = getWidth();
        int screenStart = getScrollX();
        int screenEnd = screenStart + width;

        int scrollXDelta = 0;

        if (rect.right > screenEnd && rect.left > screenStart) {

            if (rect.width() > width) {
                scrollXDelta += (rect.left - screenStart);
            } else {
                scrollXDelta += (rect.right - screenEnd);
            }

            int end = getChildAt(0).getRight();
            int distanceToEnd = end - screenEnd;
            scrollXDelta = Math.min(scrollXDelta, distanceToEnd);

        } else if (rect.left < screenStart && rect.right < screenEnd) {

            if (rect.width() > width) {
                scrollXDelta -= (screenEnd - rect.right);
            } else {
                scrollXDelta -= (screenStart - rect.left);
            }

            scrollXDelta = Math.max(scrollXDelta, -getScrollX());
        }
        return scrollXDelta;
    }

    @Override
    public boolean requestChildRectangleOnScreen(@Nonnull View child, @Nonnull Rect rectangle,
                                                 boolean immediate) {
        // offset into coordinate space of this scroll view
        rectangle.offset(child.getLeft() - child.getScrollX(),
                child.getTop() - child.getScrollY());

        return scrollToChildRect(rectangle, immediate);
    }

    @Override
    public void requestLayout() {
        mIsLayoutDirty = true;
        super.requestLayout();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        mIsLayoutDirty = false;
        // Give a child focus if it needs it
        if (mChildToScrollTo != null && isViewDescendantOf(mChildToScrollTo, this)) {
            scrollToDescendant(mChildToScrollTo);
        }
        mChildToScrollTo = null;

        if (!isLaidOut()) {
            final int childHeight = (getChildCount() > 0) ? getChildAt(0).getMeasuredHeight() : 0;
            final int childWidth = (getChildCount() > 0) ? getChildAt(0).getMeasuredWidth() : 0;

            final int scrollRangeY = Math.max(0,
                    childHeight - (b - t - mPaddingBottom - mPaddingTop));

            final int scrollRangeX = Math.max(0,
                    childWidth - (l - r - mPaddingLeft - mPaddingRight));


            // Don't forget to clamp
            if (mScrollY > scrollRangeY) {
                mScrollY = scrollRangeY;
            } else if (mScrollY < 0) {
                mScrollY = 0;
            }

            if (mScrollX > scrollRangeX) {
                mScrollX = scrollRangeX;
            } else if (mScrollX < 0) {
                mScrollX = 0;
            }
        }

        // Calling this with the present values causes it to re-claim them
        scrollTo(mScrollX, mScrollY);

        if (notCentered) {
            scrollToCenter();
            notCentered = false;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int prevWidth, int prevHeight) {
        super.onSizeChanged(w, h, prevWidth, prevHeight);

        View currentFocused = findFocus();
        if (currentFocused == null || currentFocused == this) {
            return;
        }

        // If the currently-focused view was visible on the screen when the
        // screen was at the old height, then scroll the screen to make that
        // view visible with the new screen height.
        if (isWithinDeltaOfScreen(currentFocused, 0, prevHeight)) {
            currentFocused.getDrawingRect(mTempRect);
            offsetDescendantRectToMyCoords(currentFocused, mTempRect);
            int scrollDeltaX = computeScrollXDeltaToGetChildRectOnScreen(mTempRect);
            int scrollDeltaY = computeScrollYDeltaToGetChildRectOnScreen(mTempRect);
            if (scrollDeltaX != 0 || scrollDeltaY != 0) {
                smoothScrollBy(scrollDeltaX, scrollDeltaY);
            }
        }
    }

    /**
     * Return true if child is a descendant of parent, (or equal to the parent).
     */
    private static boolean isViewDescendantOf(View child, View parent) {
        if (child == parent) {
            return true;
        }

        final ViewParent theParent = child.getParent();
        return (theParent instanceof ViewGroup) && isViewDescendantOf((View) theParent, parent);
    }

    private void endDrag() {
        mIsBeingDragged = false;

        recycleVelocityTracker();
    }

    public void scrollToCenter() {
        scrollTo(getXScrollRange()/2, getYScrollRange()/2);
    }

    /**
     * {@inheritDoc}
     *
     * <p>This version also clamps the scrolling to the bounds of our child.
     */
    @Override
    public void scrollTo(int x, int y) {
        // we rely on the fact the View.scrollBy calls scrollTo.
        if (getChildCount() > 0) {
            View child = getChildAt(0);
            x = clamp(x, getWidth() - mPaddingRight - mPaddingLeft, child.getWidth());
            y = clamp(y, getHeight() - mPaddingBottom - mPaddingTop, child.getHeight());
            if (x != mScrollX || y != mScrollY) {
                super.scrollTo(x, y);
            }
        }
    }

    @Override
    public boolean onStartNestedScroll(@Nonnull View child, @Nonnull View target, int axes, int type) {
        return (axes & SCROLL_AXIS_VERTICAL) != 0 || (axes & SCROLL_AXIS_HORIZONTAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(@Nonnull View child, @Nonnull View target, int axes, int type) {
        super.onNestedScrollAccepted(child, target, axes, type);
        startNestedScroll(SCROLL_AXIS_VERTICAL | SCROLL_AXIS_HORIZONTAL, type);
    }

    @Override
    public void onNestedScroll(@Nonnull View target, int dxConsumed, int dyConsumed, int dxUnconsumed,
                               int dyUnconsumed, int type, @Nonnull int[] consumed) {
        final int oldScrollX = mScrollX;
        final int oldScrollY = mScrollY;
        scrollBy(dxUnconsumed, dyUnconsumed);

        final int mxConsumed = mScrollX - oldScrollX;
        final int mxUnconsumed = dxUnconsumed - mxConsumed;

        final int myConsumed = mScrollY - oldScrollY;
        final int myUnconsumed = dyUnconsumed - myConsumed;

        consumed[0] += mxConsumed;
        consumed[1] += myConsumed;

        dispatchNestedScroll(mxConsumed, myConsumed, mxUnconsumed, myUnconsumed, null, type, consumed);
    }

    private static int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            /* my >= child is this case:
             *                    |--------------- me ---------------|
             *     |------ child ------|
             * or
             *     |--------------- me ---------------|
             *            |------ child ------|
             * or
             *     |--------------- me ---------------|
             *                                  |------ child ------|
             *
             * n < 0 is this case:
             *     |------ me ------|
             *                    |-------- child --------|
             *     |-- mScrollX --|
             */
            return 0;
        }
        if ((my + n) > child) {
            /* this case:
             *                    |------ me ------|
             *     |------ child ------|
             *     |-- mScrollX --|
             */
            return child - my;
        }
        return n;
    }
}
