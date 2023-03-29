package com.rodev.jbpcore.fragment.editor;

import com.rodev.jbpcore.Colors;
import com.rodev.jbpcore.blueprint.pin.default_input_value.CustomArrayAdapter;
import com.rodev.jbpcore.utils.MaterialButtonBackground;
import com.rodev.jbpcore.utils.ParamsBuilder;
import com.rodev.jbpcore.view.MaterialButton;
import com.rodev.jbpcore.workspace.compiler.CodeCompiler;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.math.Rect;
import icyllis.modernui.widget.RelativeLayout;
import icyllis.modernui.widget.Spinner;
import lombok.RequiredArgsConstructor;

import javax.annotation.Nonnull;

public class CompileButton extends MaterialButton {

    private final Spinner compileSetting = new Spinner();

    public CompileButton() {
        super();

        initCompileSetting();

        container.addView(compileSetting);
    }

    private void initCompileSetting() {
        compileSetting.setBackground(new Drawable() {
            private final int mRadius = dp(4);

            @Override
            public void draw(@Nonnull Canvas canvas) {
                Paint paint = Paint.get();
                paint.setColor(Colors.WHITE);
                paint.setStyle(Paint.STROKE);
                paint.setStrokeWidth(1.2f);
                Rect b = getBounds();
                canvas.drawRoundRect(b.left, b.top, b.right, b.bottom, mRadius, paint);
            }

            @Override
            public boolean getPadding(@Nonnull Rect padding) {
                int r = (int) Math.ceil(mRadius / 2.5f);
                padding.set(r, r, r, r);
                return true;
            }
        });
        var adapter = new CustomArrayAdapter<>(CompileModeWrapper.values());

        compileSetting.setAdapter(adapter);
        ParamsBuilder.using(RelativeLayout.LayoutParams::new)
                .wrapContent()
                .setup(p -> p.leftMargin = dp(6))
                .applyTo(compileSetting);
    }

    public CodeCompiler.CompileMode getCompileMode() {
        var wrapper = (CompileModeWrapper) compileSetting.getSelectedItem();

        if(wrapper == null) throw new IllegalStateException("Shouldn't get here.");

        return wrapper.getCompileMode();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        compileSetting.setEnabled(enabled);
    }

    @RequiredArgsConstructor
    private enum CompileModeWrapper {
        UPLOAD("В облако", CodeCompiler.CompileMode.COMPILE_AND_UPLOAD),
        TO_FILE("В файл", CodeCompiler.CompileMode.COMPILE_TO_FILE)

        ;

        private final String name;
        private final CodeCompiler.CompileMode compileMode;

        @Override
        public String toString() {
            return name;
        }

        public CodeCompiler.CompileMode getCompileMode() {
            return compileMode;
        }
    }

}
