package com.rodev.test.blueprint.pin.default_input_value;

import com.rodev.test.utils.TextViewCreationListener;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.ArrayAdapter;
import icyllis.modernui.widget.TextView;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CustomArrayAdapter<T> extends ArrayAdapter<T> {

    public CustomArrayAdapter(@NotNull T[] objects) {
        super(objects);
    }

    public CustomArrayAdapter(@NotNull List<T> objects) {
        super(objects);
    }

    @Nonnull
    @Override
    public View getView(int position, @Nullable View convertView, @Nonnull ViewGroup parent) {
        return createViewInner(position, convertView);
    }

    @Nonnull
    private View createViewInner(int position, @Nullable View convertView) {
        final TextView text;

        if (convertView == null) {
            text = new TextView();
        } else {
            text = (TextView) convertView;
        }

        final T item = getItem(position);
        if (item instanceof CharSequence) {
            text.setText((CharSequence) item);
        } else {
            text.setText(String.valueOf(item));
        }

        TextViewCreationListener.onArrayAdapterTextItemCreated(text);

        text.setTextSize(14);
        text.setTextAlignment(View.TEXT_ALIGNMENT_VIEW_START);
        final int dp4 = View.dp(4);
        text.setPadding(dp4, dp4, dp4, dp4);
        text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return text;
    }
}
