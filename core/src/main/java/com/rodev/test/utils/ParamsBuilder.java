package com.rodev.test.utils;

import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

import static icyllis.modernui.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static icyllis.modernui.view.ViewGroup.LayoutParams.WRAP_CONTENT;

@RequiredArgsConstructor(staticName = "using")
public class ParamsBuilder<T extends ViewGroup.LayoutParams> {

    private final LayoutParamsSupplier<T> layoutParamsSupplier;

    private T params;

    public ParamsBuilder<T> wrapContent() {
        params = layoutParamsSupplier.create(WRAP_CONTENT, WRAP_CONTENT);
        return this;
    }

    public ParamsBuilder<T> matchParent() {
        params = layoutParamsSupplier.create(MATCH_PARENT, MATCH_PARENT);
        return this;
    }

    public ParamsBuilder<T> widthWrapContent() {
        return width(WRAP_CONTENT);
    }

    public ParamsBuilder<T> heightWrapContent() {
        return height(WRAP_CONTENT);
    }

    public ParamsBuilder<T> widthMatchParent() {
        return width(MATCH_PARENT);
    }

    public ParamsBuilder<T> heightMatchParent() {
        return height(MATCH_PARENT);
    }

    public ParamsBuilder<T> width(int width) {
        createIfNull();
        params.width = width;
        return this;
    }

    public ParamsBuilder<T> height(int height) {
        createIfNull();
        params.height = height;
        return this;
    }

    public ParamsBuilder<T> squareShape(int length) {
        params = layoutParamsSupplier.create(length, length);
        return this;
    }

    public ParamsBuilder<T> setup(Consumer<T> layoutParamsConsumer) {
        createIfNull();
        layoutParamsConsumer.accept(params);

        return this;
    }

    public T applyTo(View view) {
        createIfNull();
        view.setLayoutParams(params);

        return params;
    }

    private void createIfNull() {
        if(params == null) {
            params = layoutParamsSupplier.create(0, 0);
        }
    }

    public interface LayoutParamsSupplier<T extends ViewGroup.LayoutParams> {

        T create(int width, int height);

    }
}
