package com.rodev.jbpcore.view;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.view.Gravity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CompoundImageDrawable extends Drawable {
    private Rect mSrcRect;
    private final Rect destRectForFirstImage = new Rect();
    private final Rect destRectForSecondImage = new Rect();

    private ImageState mImageState;
    private int mBlendColor;

    private final boolean mFullImage = true;
    private boolean mDstRectAndInsetsDirty = true;
    private boolean mMutated;

    public CompoundImageDrawable(TintedImage first, TintedImage second, double distanceRatio) {
        init(new ImageState(first, second, distanceRatio));
    }

    @Nonnull
    public final Paint getPaint() {
        return mImageState.mPaint;
    }

    public int getGravity() {
        return mImageState.mGravity;
    }

    public void setGravity(int gravity) {
        if (mImageState.mGravity != gravity) {
            mImageState.mGravity = gravity;
            mDstRectAndInsetsDirty = true;
            invalidateSelf();
        }
    }

    @Override
    public void setAutoMirrored(boolean mirrored) {
        if (mImageState.mAutoMirrored != mirrored) {
            mImageState.mAutoMirrored = mirrored;
            invalidateSelf();
        }
    }

    public TintedImage getLeftImage() {
        return mImageState.firstImage;
    }

    public TintedImage getRightImage() {
        return mImageState.secondImage;
    }

    @Override
    public final boolean isAutoMirrored() {
        return mImageState.mAutoMirrored;
    }

    @Override
    protected void onBoundsChange(@Nonnull Rect bounds) {
        mDstRectAndInsetsDirty = true;
    }

    private void updateDstRectAndInsetsIfDirty() {
        if (mDstRectAndInsetsDirty) {
            final Rect b = getBounds();

            double widthRatio = ((double) b.width()) * mImageState.distanceRatio;

            int firstImageRight = (int) widthRatio;
            int secondImageLeft = b.left + firstImageRight;

            destRectForFirstImage.set(b.left, b.top, firstImageRight, b.bottom);
            destRectForSecondImage.set(secondImageLeft, b.top, b.right, b.bottom);
        }
    }

    @Override
    public void draw(@Nonnull Canvas canvas) {
        final Image image = mImageState.firstImage.getImage();
        if (image == null) {
            return;
        }

        updateDstRectAndInsetsIfDirty();

        final ImageState state = mImageState;
        final Paint paint = state.mPaint;

        paint.setColor(state.firstImage.getTint());
        canvas.drawImage(state.firstImage.getImage(), null, destRectForFirstImage, paint);

        paint.setColor(state.secondImage.getTint());
        canvas.drawImage(state.secondImage.getImage(), null, destRectForSecondImage, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        final int oldAlpha = mImageState.mPaint.getAlpha();
        if (alpha != oldAlpha) {
            mImageState.mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public int getAlpha() {
        return mImageState.mPaint.getAlpha();
    }

    @Override
    public void setTintList(@Nullable ColorStateList tint) {
        final ImageState state = mImageState;
        if (state.mTint != tint) {
            state.mTint = tint;
            if (tint == null) {
                mBlendColor = ~0;
            } else {
                mBlendColor = tint.getColorForState(getState(), ~0);
            }
            invalidateSelf();
        }
    }

    @Nonnull
    @Override
    public Drawable mutate() {
        if (!mMutated && super.mutate() == this) {
            mImageState = new ImageState(mImageState);
            mMutated = true;
        }
        return this;
    }

    @Override
    public void clearMutated() {
        super.clearMutated();
        mMutated = false;
    }

    @Override
    protected boolean onStateChange(@Nonnull int[] stateSet) {
        final ImageState state = mImageState;
        if (state.mTint != null) {
            mBlendColor = state.mTint.getColorForState(stateSet, ~0);
            return true;
        }
        return false;
    }

    @Override
    public boolean isStateful() {
        return (mImageState.mTint != null && mImageState.mTint.isStateful())
                || super.isStateful();
    }

    @Override
    public boolean hasFocusStateSpecified() {
        return mImageState.mTint != null && mImageState.mTint.hasFocusStateSpecified();
    }

    @Override
    public int getIntrinsicWidth() {
        if (mImageState.firstImage == null) {
            return super.getIntrinsicWidth();
        }
        if (mFullImage) {
            return mImageState.firstImage.getWidth();
        }
        return Math.min(mSrcRect.width(), mImageState.firstImage.getWidth());
    }

    @Override
    public int getIntrinsicHeight() {
        if (mImageState.firstImage == null) {
            return super.getIntrinsicHeight();
        }
        if (mFullImage) {
            return mImageState.firstImage.getHeight();
        }
        return Math.min(mSrcRect.height(), mImageState.firstImage.getHeight());
    }

    @Override
    public final ConstantState getConstantState() {
        return mImageState;
    }

    static final class ImageState extends ConstantState {

        final Paint mPaint;

        TintedImage firstImage;
        TintedImage secondImage;
        ColorStateList mTint = null;

        int mGravity = Gravity.FILL;

        boolean mAutoMirrored = false;

        double distanceRatio;

        ImageState(TintedImage first, TintedImage second, double distanceRatio) {
            firstImage = first;
            secondImage = second;
            mPaint = new Paint();
            this.distanceRatio = distanceRatio;
        }

        ImageState(@Nonnull ImageState imageState) {
            firstImage = imageState.firstImage;
            secondImage = imageState.secondImage;
            mTint = imageState.mTint;
            mGravity = imageState.mGravity;
            mPaint = new Paint(imageState.mPaint);
            mAutoMirrored = imageState.mAutoMirrored;
            distanceRatio = imageState.distanceRatio;
        }

        @Nonnull
        @Override
        public Drawable newDrawable() {
            return new CompoundImageDrawable(this);
        }
    }

    private CompoundImageDrawable(ImageState state) {
        init(state);
    }

    private void init(ImageState state) {
        mImageState = state;
        updateLocalState();
    }

    private void updateLocalState() {
        if (mImageState.mTint == null) {
            mBlendColor = ~0;
        } else {
            mBlendColor = mImageState.mTint.getColorForState(getState(), ~0);
        }
    }
}
