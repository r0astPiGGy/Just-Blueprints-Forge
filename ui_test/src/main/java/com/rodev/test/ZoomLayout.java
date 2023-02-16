package com.rodev.test;

import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.RelativeLayout;
import org.jetbrains.annotations.NotNull;

import javax.naming.Context;

public class ZoomLayout extends RelativeLayout {
//
//    private enum Mode {
//        NONE,
//        DRAG,
//        ZOOM
//    }
//
//    private static final String TAG = "ZoomLayout";
//    private static final float MIN_ZOOM = 1.0f;
//    private static final float MAX_ZOOM = 4.0f;
//
//    private Mode mode = Mode.NONE;
//    private float scale = 1.0f;
//    private float lastScaleFactor = 0f;
//
//    // Where the finger first  touches the screen
//    private float startX = 0f;
//    private float startY = 0f;
//
//    // How much to translate the canvas
//    private float dx = 0f;
//    private float dy = 0f;
//    private float prevDx = 0f;
//    private float prevDy = 0f;
//
//    public ZoomLayout() {
//        setOnTouchListener((v, motionEvent) -> {
//            switch (motionEvent.getAction() & 255) {
//                case MotionEvent.ACTION_DOWN:
//                    if (scale > MIN_ZOOM) {
//                        mode = Mode.DRAG;
//                        startX = motionEvent.getX() - prevDx;
//                        startY = motionEvent.getY() - prevDy;
//                    }
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    if (mode == Mode.DRAG) {
//                        dx = motionEvent.getX() - startX;
//                        dy = motionEvent.getY() - startY;
//                    }
//                    break;
//                case MotionEvent.ACTION_UP:
//                    mode = Mode.NONE;
//                    prevDx = dx;
//                    prevDy = dy;
//                    break;
//            }
//            //scaleDetector.onTouchEvent(motionEvent);
//
//            if ((mode == Mode.DRAG && scale >= MIN_ZOOM) || mode == Mode.ZOOM) {
//                getParent().requestDisallowInterceptTouchEvent(true);
//                float maxDx = (child().getWidth() - (child().getWidth() / scale)) / 2 * scale;
//                float maxDy = (child().getHeight() - (child().getHeight() / scale))/ 2 * scale;
//                dx = Math.min(Math.max(dx, -maxDx), maxDx);
//                dy = Math.min(Math.max(dy, -maxDy), maxDy);
//                System.out.println("Width: " + child().getWidth() + ", scale " + scale + ", dx " + dx
//                        + ", max " + maxDx);
//                applyScaleAndTranslation();
//            }
//
//            return true;
//        });
//    }
//
//    @Override
//    public boolean onGenericMotionEvent(@NotNull MotionEvent event) {
//        event.get
//        return super.onGenericMotionEvent(event);
//    }
//
//    @Override
//    public boolean onScale(ScaleGestureDetector scaleDetector) {
//        float scaleFactor = scaleDetector.getScaleFactor();
//        Log.i(TAG, "onScale" + scaleFactor);
//        if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
//            scale *= scaleFactor;
//            scale = Math.max(MIN_ZOOM, Math.min(scale, MAX_ZOOM));
//            lastScaleFactor = scaleFactor;
//        } else {
//            lastScaleFactor = 0;
//        }
//        return true;
//    }
//
//    private void applyScaleAndTranslation() {
//        child().setScaleX(scale);
//        child().setScaleY(scale);
//        child().setTranslationX(dx);
//        child().setTranslationY(dy);
//    }
//
//    private View child() {
//        return getChildAt(0);
//    }

}
