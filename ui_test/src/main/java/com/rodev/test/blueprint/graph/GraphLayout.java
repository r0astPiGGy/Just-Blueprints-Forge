package com.rodev.test.blueprint.graph;

import com.rodev.test.blueprint.data.action.Action;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.view.*;
import icyllis.modernui.widget.AbsoluteLayout;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public class GraphLayout extends AbsoluteLayout implements ViewMoveListener {

    private final List<Runnable> coordinateRecalculationCallbacks = new LinkedList<>();
    private boolean laidOut = false;

    private GraphController graphController;
    private DrawListener drawListener;
    private GraphTouchListener graphTouchListener;
    private ContextMenuOpenListener contextMenuOpenListener;

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

    public void add(View view) {
        add(view, 0, 0, Positioning.ORIGIN);
    }

    // TODO: Rename
    public void add(View view, int x, int y, Positioning positioning) {
        if(!laidOut) {
            coordinateRecalculationCallbacks.add(()-> {
                var layout = (LayoutParams) view.getLayoutParams();
                setCoordinatesRelativelyToPositioning(layout, x, y, positioning);
                view.requestLayout();
            });
        }
        _add(view, x, y, positioning);
    }

    private void _add(View view, int x, int y, Positioning positioning) {
        var oldLayoutParams = view.getLayoutParams();
        if(oldLayoutParams == null) {
            oldLayoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        var layoutParams = new AbsoluteLayout.LayoutParams(oldLayoutParams);

        setCoordinatesRelativelyToPositioning(layoutParams, x, y, positioning);

        addView(view, layoutParams);
    }

    private void setCoordinatesRelativelyToPositioning(
            LayoutParams layoutParams, int x, int y, Positioning positioning
    ) {
        var xOffset = getWidth() / 2;
        var yOffset = getHeight() / 2;

        layoutParams.x = positioning.calculateX(xOffset, x);
        layoutParams.y = positioning.calculateY(yOffset, y);
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

        graphController.navigateTo(x, y);
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

//        boolean shouldNotProcess = super.onTouchEvent(event);
//
//        if(shouldNotProcess) return true;

        if(event.getAction() == MotionEvent.ACTION_MOVE) return false;

        if(event.getButtonState() == MotionEvent.BUTTON_BACK) {
            navigateTo(nextChild());
        }

        if(event.getButtonState() == MotionEvent.BUTTON_SECONDARY) {
            var x = event.getX();
            var y = event.getY();

            if(contextMenuOpenListener != null) {
                contextMenuOpenListener.onContextMenuOpen(type -> {
                    var created = graphController.createViewAt((int) x, (int) y, type);
                    _add(created, (int) x, (int) y, Positioning.GLOBAL);
                    created.requestLayout();
                }, this, x, y);
            }
        }

        if(event.getButtonState() == MotionEvent.BUTTON_PRIMARY) {
            if(graphTouchListener != null) {
                graphTouchListener.onGraphTouch(event.getX(), event.getY());
            }
        }

        return true;
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

    enum Positioning {
        ORIGIN {
            @Override
            public int calculateX(int offset, int x) {
                return offset + x;
            }

            @Override
            public int calculateY(int offset, int y) {
                return offset + y;
            }
        },
        GLOBAL

        ;

        public int calculateX(int offset, int x) {
            return x;
        }

        public int calculateY(int offset, int y) {
            return y;
        }
    }

    @FunctionalInterface
    interface LineDrawCallback {
        void draw(Canvas canvas, Paint paint);
    }
}
