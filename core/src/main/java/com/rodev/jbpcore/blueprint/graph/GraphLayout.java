package com.rodev.jbpcore.blueprint.graph;

import com.rodev.jbpcore.blueprint.Navigable;
import com.rodev.jbpcore.contextmenu.ContextMenuBuilder;
import com.rodev.jbpcore.workspace.Blueprint;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.view.*;
import icyllis.modernui.widget.AbsoluteLayout;
import icyllis.modernui.widget.FrameLayout;
import org.jetbrains.annotations.NotNull;

public class GraphLayout extends AbsoluteLayout implements ViewMoveListener, ContextMenuBuilderProvider, ViewHolder {
    private Navigable navigator;
    private GraphController graphController;
    private ContextMenuOpenHandler contextMenuOpenHandler;

    public GraphLayout() {
        var params = new FrameLayout.LayoutParams(
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        );
        setLayoutParams(params);
    }

    public void setGraphController(GraphController graphController) {
        this.graphController = graphController;
        this.graphController.setInvalidationCallback(this::invalidate);
        this.graphController.setViewHolder(this);
    }

    public void setContextMenuOpenHandler(ContextMenuOpenHandler contextMenuOpenHandler) {
        this.contextMenuOpenHandler = contextMenuOpenHandler;
    }

    public void setNavigator(Navigable navigator) {
        this.navigator = navigator;
    }

    public void loadBlueprint(Blueprint blueprint) {
        blueprint.loadTo(graphController);
    }

    private void addNodeInternal(View view, int x, int y) {
        var oldLayoutParams = view.getLayoutParams();
        if (oldLayoutParams == null) {
            oldLayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        var layoutParams = new AbsoluteLayout.LayoutParams(oldLayoutParams);

        layoutParams.x = x;
        layoutParams.y = y;

        addView(view, layoutParams);
    }

    public void navigateTo(int childIndex) {
        View view = getChildAt(childIndex);
        var layout = (LayoutParams) view.getLayoutParams();

        var x = layout.x + (view.getWidth() / 2);
        var y = layout.y + (view.getWidth() / 2);

        navigator.navigateTo(x, y);
    }

    private int childIterator = 0;

    // Only for debug
    private int nextChild() {
        int childCount = getChildCount();
        int old = childIterator;
        childIterator++;
        if(childCount <= childIterator)
            childIterator = 0;

        return old;
    }

    @Override
    public boolean onTouchEvent(@NotNull MotionEvent event) {
        if(!event.isTouchEvent()) return false;

        if(event.getAction() == MotionEvent.ACTION_MOVE) return false;

        if(event.getButtonState() == MotionEvent.BUTTON_BACK) {
            navigateTo(nextChild());
        }

        if(event.getButtonState() == MotionEvent.BUTTON_SECONDARY) {
            var x = event.getX();
            var y = event.getY();

            openContextMenu((int) x, (int) y);
        }

        if(event.getButtonState() == MotionEvent.BUTTON_PRIMARY) {
            graphController.onGraphTouch(event.getX(), event.getY());
        }

        return true;
    }

    private void openContextMenu(int x, int y) {
        graphController.onContextMenuOpen(x, y);
    }

    public void addViewAt(View view, int x, int y) {
        addNodeInternal(view, x, y);
        view.requestLayout();
    }

    @Override
    protected void onDraw(@NotNull Canvas canvas) {
        graphController.onDraw(canvas, Paint.take());

        super.onDraw(canvas);
    }

    @Override
    public void onMove(View view, int x, int y) {
        if(view.getLayoutParams() instanceof AbsoluteLayout.LayoutParams params) {
            params.x = x;
            params.y = y;
            view.requestLayout();
        }
    }

    @Override
    public ContextMenuBuilder createBuilder(float x, float y) {
        if(contextMenuOpenHandler == null) {
            throw new IllegalStateException("Tried to open context menu, but there is no handler...");
        }
        return contextMenuOpenHandler.createBuilder(this, x, y);
    }
}
