package com.rodev.jbpcore.ui.contextmenu;

import com.rodev.jbpcore.data.DataAccess;
import com.rodev.jbpcore.data.action.Action;
import com.rodev.jbpcore.ui.contextmenu.tree.ContextTreeNodeView;
import com.rodev.jbpcore.ui.contextmenu.tree.ContextTreeRootView;
import icyllis.modernui.math.Rect;
import icyllis.modernui.view.*;
import icyllis.modernui.widget.*;

import javax.annotation.Nonnull;

import static icyllis.modernui.view.View.dp;

public class BlueprintPopup implements PopupWindow.OnDismissListener {

    private final BlueprintContextMenu mPopup;
    private Rect mEpicenterBounds;
    private final ContextTreeRootView treeRootView = new ContextTreeRootView();

    private final ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new OnGlobalLayoutListener();
    private final View.OnAttachStateChangeListener mAttachStateChangeListener = new OnAttachStateChangeListener();

    private View mAnchorView;
    private View mShownAnchorView;
    private ViewTreeObserver mTreeObserver;
    private final ContextMenuBuilder builder;

    /**
     * Whether the popup has been dismissed. Once dismissed, it cannot be opened again.
     */
    private boolean mWasDismissed;

    private int mDropDownGravity = Gravity.NO_GRAVITY;

    public BlueprintPopup(ContextMenuBuilder builder, @Nonnull View anchorView) {
        this.builder = builder;

        mAnchorView = anchorView;

        mPopup = new BlueprintContextMenu(dp(400), dp(400), this::createPopupContent);
    }

    private RelativeLayout createPopupContent() {
        var popupRoot = new ContextMenuRootView();

        popupRoot.setHeader(builder.header);

        onContextTreeFill();

        var scrollView = popupRoot.getScrollView();
        scrollView.addView(treeRootView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        var searchView = popupRoot.getSearchView();
        searchView.setOnTextChangedListener(changedText -> {
            var textToFind = changedText.toLowerCase();
            treeRootView.hideIfNot(item -> item.getName().toLowerCase().contains(textToFind));
        });

        return popupRoot;
    }

    public void onContextTreeFill() {
        var actionRegistry = DataAccess.getInstance().actionRegistry;

        for(var action : actionRegistry.getAll()) {
            var invalid = builder.actionFilter.test(action);

            if(invalid) continue;

            var item = createItem(action.name(), action);

            addItemToTreeRootView(treeRootView, item, action.category());
        }
    }

    private void addItemToTreeRootView(ContextTreeRootView rootView, Item item, String category) {
        ContextTreeNodeView treeNode = null;
        for (String id : category.split("\\.")) {
            if (treeNode == null) {
                treeNode = rootView.getOrCreate(id);
            } else {
                treeNode = treeNode.getOrCreate(id);
            }
            var translated = DataAccess.translateCategoryId(id);
            if (translated == null) translated = id;

            treeNode.setName(translated);
        }

        if (treeNode == null) return;

        treeNode.add(item);
    }

    public void setEpicenterBounds(Rect bounds) {
        mEpicenterBounds = bounds;
    }

    public Rect getEpicenterBounds() {
        return mEpicenterBounds;
    }

    public Item createItem(String displayText, Action action) {
        return Item.of(displayText, action.createIcon(), () -> {
            dismiss();
            builder.onClick.accept(action);
        });
    }

    public void setGravity(int gravity) {
        mDropDownGravity = gravity;
    }

    public void show() {
        if (mWasDismissed || mAnchorView == null) {
            throw new IllegalStateException("Popup cannot be used without an anchor");
        }

        tryShow();
    }

    private void tryShow() {
        if (isShowing()) {
            return;
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
    }

    public void dismiss() {
        if (isShowing()) {
            mPopup.dismiss();
        }
    }

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
    }

    public void setAnchorView(View anchor) {
        mAnchorView = anchor;
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener) {
        mPopup.setDismissListener(() -> {
            onDismiss();
            listener.onDismiss();
            builder.onDismiss.run();
        });
    }

    public void setHorizontalOffset(int x) {
        mPopup.setHorizontalOffset(x);
    }

    public void setVerticalOffset(int y) {
        mPopup.setVerticalOffset(y);
    }

    private class OnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
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
    }

    private class OnAttachStateChangeListener implements View.OnAttachStateChangeListener {
        @Override
        public void onViewAttachedToWindow(View v) {}

        @Override
        public void onViewDetachedFromWindow(View v) {
            if (mTreeObserver != null) {
                if (!mTreeObserver.isAlive()) mTreeObserver = v.getViewTreeObserver();
                mTreeObserver.removeOnGlobalLayoutListener(mGlobalLayoutListener);
            }
            v.removeOnAttachStateChangeListener(this);
        }
    }
}
