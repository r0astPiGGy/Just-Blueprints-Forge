package com.rodev.test.contextmenu;

import com.rodev.test.Colors;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.TextWatcher;
import icyllis.modernui.view.*;
import icyllis.modernui.view.menu.*;
import icyllis.modernui.widget.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static icyllis.modernui.view.View.dp;

public class BlueprintMenuPopup extends MenuPopup implements PopupWindow.OnDismissListener {

    private final BPContextMenu mPopup;

    private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener =
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    // Only move the popup if it's showing and non-modal. We don't want
                    // to be moving around the only interactive window, since there's a
                    // good chance the user is interacting with it.
                    if (isShowing() && !mPopup.isModal()) {
                        final View anchor = mShownAnchorView;
                        if (anchor == null || !anchor.isShown()) {
                            dismiss();
                        } else {
                            // Recompute window size and position
                            mPopup.show();
                        }
                    }
                }
            };

    private final View.OnAttachStateChangeListener mAttachStateChangeListener =
            new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    if (mTreeObserver != null) {
                        if (!mTreeObserver.isAlive()) mTreeObserver = v.getViewTreeObserver();
                        mTreeObserver.removeOnGlobalLayoutListener(mGlobalLayoutListener);
                    }
                    v.removeOnAttachStateChangeListener(this);
                }
            };

    private PopupWindow.OnDismissListener mOnDismissListener;

    private View mAnchorView;
    private View mShownAnchorView;
    private Callback mPresenterCallback;
    private ViewTreeObserver mTreeObserver;

    /**
     * Whether the popup has been dismissed. Once dismissed, it cannot be opened again.
     */
    private boolean mWasDismissed;

    private int mDropDownGravity = Gravity.NO_GRAVITY;

    public BlueprintMenuPopup(@Nonnull View anchorView) {
        mAnchorView = anchorView;
        mPopup = new BPContextMenu(400, 400, () -> {
            var popupRoot = new RelativeLayout() { @Override public boolean onTouchEvent(@NotNull MotionEvent event) {return true;} };
            popupRoot.setGravity(Gravity.TOP | Gravity.CENTER);
            int headerId = 45145;
            int searchBarId = 454551;
            var treeRoot = new ContextTreeRootView();
            {
                var headerLabel = new TextView();
                headerLabel.setId(headerId);
                headerLabel.setText("All actions for this Blueprint");
                headerLabel.setTextSize(View.sp(17));
                var params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.ALIGN_PARENT_START);
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                popupRoot.addView(headerLabel, params);
            }
            {
                var textField = new EditText();
                textField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        var textToFilter = s.toString();
                        treeRoot.hideIfNot(item -> {
                            return item.getName().toLowerCase().contains(textToFilter.toLowerCase());
                        });
                    }
                });
                textField.setId(454551);
                textField.setHint("Search");
                textField.setTextSize(View.sp(13));
                textField.setBackground(new Drawable() {
                    private final int mRadius = dp(4);

                    @Override
                    public void draw(@Nonnull Canvas canvas) {
                        Paint paint = Paint.get();
                        paint.setColor(Colors.WHITE);
                        Rect b = getBounds();
                        canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, mRadius, paint);
                    }

                    @Override
                    public boolean getPadding(@Nonnull Rect padding) {
                        int r = (int) Math.ceil(mRadius / 2f);
                        padding.set(r, r, r, r);
                        return true;
                    }
                });
                textField.setTextColor(Colors.NODE_BACKGROUND);
                var params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.addRule(RelativeLayout.BELOW, headerId);
                params.setMargins(0, 5, 0, 5);
                popupRoot.addView(textField, params);
            }
            {
                var scrollView = new ScrollView();
                {
                    var events = treeRoot.getOrCreate("Events");
                    events.add("Startup event");
                    events.add("Bebra event");
                    var functions = treeRoot.getOrCreate("Functions");
                    var worldFuncs = functions.getOrCreate("World");
                    worldFuncs.add("Spawn entity at location");
                    worldFuncs.add("Set block at location");
                    functions.add("Print string");
                    functions.add("Print bebr");
                    functions.add("Bebr sup");

                    scrollView.addView(treeRoot, new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    ));
                }
                var params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );
                params.addRule(RelativeLayout.BELOW, searchBarId);
                popupRoot.addView(scrollView, params);
            }
            popupRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            return popupRoot;
        });
        mPopup.setBackgroundDrawable(new Drawable() {
            //private final int mRadius = dp(8);

            @Override
            public void draw(@Nonnull Canvas canvas) {
                Paint paint = Paint.get();
                paint.setColor(Colors.NODE_BACKGROUND);
                Rect b = getBounds();
                canvas.drawRect(b.left, b.top, b.right, b.bottom, paint);
            }

            @Override
            public boolean getPadding(@Nonnull Rect padding) {
                int r = (int) Math.ceil(8);
                padding.set(r, r, r, r);
                return true;
            }
        });
        mPopup.setOverlapAnchor(true);
    }

    @Override
    public void setForceShowIcon(boolean forceShow) {}

    @Override
    public void setGravity(int gravity) {
        mDropDownGravity = gravity;
    }

    private boolean tryShow() {
        if (isShowing()) {
            return true;
        }

        if (mWasDismissed || mAnchorView == null) {
            return false;
        }

        mShownAnchorView = mAnchorView;
        mPopup.setModal(true);

        final View anchor = mShownAnchorView;
        final boolean addGlobalListener = mTreeObserver == null;

        mTreeObserver = anchor.getViewTreeObserver(); // Refresh to latest

        if (addGlobalListener) {
            mTreeObserver.addOnGlobalLayoutListener(mGlobalLayoutListener);
        }
        anchor.addOnAttachStateChangeListener(mAttachStateChangeListener);
        mPopup.setAnchorView(anchor);
        mPopup.setDropDownGravity(mDropDownGravity);
        mPopup.setEpicenterBounds(getEpicenterBounds());
        mPopup.show();

        return true;
    }

    @Override
    public void show() {
        if (!tryShow()) {
            throw new IllegalStateException("StandardMenuPopup cannot be used without an anchor");
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            mPopup.dismiss();
        }
    }

    @Override
    public void addMenu(MenuBuilder menu) {
        // No-op: standard implementation has only one menu which is set in the constructor.
    }

    @Override
    public boolean isShowing() {
        return !mWasDismissed && mPopup.isShowing();
    }

    @Override
    public void onDismiss() {
        mWasDismissed = true;

        if (mTreeObserver != null) {
            if (!mTreeObserver.isAlive()) mTreeObserver = mShownAnchorView.getViewTreeObserver();
            mTreeObserver.removeOnGlobalLayoutListener(mGlobalLayoutListener);
            mTreeObserver = null;
        }
        mShownAnchorView.removeOnAttachStateChangeListener(mAttachStateChangeListener);

        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }

    @Override
    public void updateMenuView(boolean cleared) {
    }

    @Override
    public void setCallback(Callback cb) {
        mPresenterCallback = cb;
    }

    @Override
    public boolean onSubMenuSelected(@Nonnull SubMenuBuilder subMenu) {
        return false;
    }

    @Override
    public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {

        dismiss();
        if (mPresenterCallback != null) {
            mPresenterCallback.onCloseMenu(menu, allMenusAreClosing);
        }
    }

    @Override
    public boolean flagActionItems() {
        return false;
    }

    @Override
    public void setAnchorView(View anchor) {
        mAnchorView = anchor;
    }

    @Override
    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    @Override
    public ListView getListView() {
        return mPopup.getListView();
    }

    @Override
    public void setHorizontalOffset(int x) {
        mPopup.setHorizontalOffset(x);
    }

    @Override
    public void setVerticalOffset(int y) {
        mPopup.setVerticalOffset(y);
    }

    @Override
    public void setShowTitle(boolean showTitle) {
    }
}