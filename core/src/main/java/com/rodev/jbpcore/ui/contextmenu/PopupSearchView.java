package com.rodev.jbpcore.ui.contextmenu;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.handlers.TextViewCreationListener;
import com.rodev.jbpcore.ui.view.DefaultTextWatcher;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.text.Editable;
import icyllis.modernui.widget.EditText;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class PopupSearchView extends EditText {
    private Consumer<String> onTextChanged = s -> {};

    public PopupSearchView(Context context) {
        super(context);
        addTextChangedListener(new TextChangedListener());
        setBackground(new Background());

        setHint("Поиск");
        TextViewCreationListener.onContextMenuSearchViewCreated(this);
    }

    public void setOnTextChangedListener(Consumer<String> textChangedListener) {
        onTextChanged = textChangedListener;
    }

    private class TextChangedListener implements DefaultTextWatcher {
        @Override
        public void afterTextChanged(Editable s) {
            var currentText = s.toString();
            onTextChanged.accept(currentText);
        }
    }

    private class Background extends Drawable {
        private final int mRadius = dp(4);

        @Override
        public void draw(@Nonnull Canvas canvas) {
            Paint paint = Paint.obtain();
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
    }

}
