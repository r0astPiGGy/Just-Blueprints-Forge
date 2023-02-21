package com.rodev.test.blueprint.graph;

import com.rodev.test.blueprint.Navigable;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.view.*;
import icyllis.modernui.widget.AbsoluteLayout;
import icyllis.modernui.widget.FrameLayout;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class GraphLayout extends AbsoluteLayout implements ViewMoveListener {

    private final List<Runnable> coordinateRecalculationCallbacks = new LinkedList<>();
    private boolean laidOut = false;

    private Navigable navigator;
    private GraphController graphController;
    private DrawListener drawListener;
    private GraphTouchListener graphTouchListener;
    private ContextMenuOpenListener contextMenuOpenListener;

    public GraphLayout() {
        var params = new FrameLayout.LayoutParams(
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
        );
        setLayoutParams(params);
    }


    public void setGraphController(GraphController graphController) {
        this.graphController = graphController;
    }

    public void setContextMenuOpenListener(ContextMenuOpenListener contextMenuOpenListener) {
        this.contextMenuOpenListener = contextMenuOpenListener;
    }

    public void setDrawListener(DrawListener drawListener) {
        this.drawListener = drawListener;
        drawListener.setInvalidationCallback(this::invalidate);
    }

    public void setNavigator(Navigable navigator) {
        this.navigator = navigator;
    }

    private void addNodeInternal(View view, int x, int y) {
        var oldLayoutParams = view.getLayoutParams();
        if(oldLayoutParams == null) {
            oldLayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        var layoutParams = new AbsoluteLayout.LayoutParams(oldLayoutParams);

        layoutParams.x = x;
        layoutParams.y = y;

        addView(view, layoutParams);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if(!laidOut) {
            laidOut = true;
            coordinateRecalculationCallbacks.forEach(Runnable::run);
            coordinateRecalculationCallbacks.clear();
        }
    }

    public void navigateTo(int childIndex) {
        View view = getChildAt(childIndex);
        var layout = (LayoutParams) view.getLayoutParams();

        var x = layout.x + (view.getWidth() / 2);
        var y = layout.y + (view.getWidth() / 2);

        navigator.navigateTo(x, y);
    }

    private int childIterator = 0;

    private int nextChild() {
        int childCount = getChildCount();
        int old = childIterator;
        childIterator++;
        if(childCount <= childIterator)
            childIterator = 0;

        return old;
    }

    @Override
    protected void onCreateContextMenu(@NotNull ContextMenu menu) {
        super.onCreateContextMenu(menu);
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
            if(graphTouchListener != null) {
                graphTouchListener.onGraphTouch(event.getX(), event.getY());
            }
        }

        return true;
    }

    private void openContextMenu(int x, int y) {
        if(contextMenuOpenListener == null) {
            System.out.println("Tried to open context menu, but have no listener...");
            return;
        }

        contextMenuOpenListener.onContextMenuOpen(type -> {
            var created = graphController.createViewAt(x, y, type);
            addNodeInternal(created, x, y);
            created.requestLayout();
        }, this, x, y);
    }

    @Override
    protected void onDraw(@NotNull Canvas canvas) {
        drawListener.onDraw(canvas, Paint.take());

        super.onDraw(canvas);
    }

    public void setGraphTouchListener(GraphTouchListener graphTouchListener) {
        this.graphTouchListener = graphTouchListener;
    }

    @Override
    public void onMove(View view, int x, int y) {
        if(view.getLayoutParams() instanceof AbsoluteLayout.LayoutParams params) {
            params.x = x;
            params.y = y;
            view.requestLayout();
        }
    }
}
