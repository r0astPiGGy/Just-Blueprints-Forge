package com.rodev.jbpcore.ui.pin.default_input_value;

import com.rodev.jbpcore.handlers.TextViewCreationListener;
import icyllis.modernui.core.Context;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewGroup;
import icyllis.modernui.widget.ArrayAdapter;
import icyllis.modernui.widget.TextView;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CustomArrayAdapter<T> extends ArrayAdapter<T> {

    private final Context context;

    public CustomArrayAdapter(Context context, @NotNull T[] objects) {
        super(context, objects);
        this.context = context;
    }

    public CustomArrayAdapter(Context context, @NotNull List<T> objects) {
        super(context, objects);
        this.context = context;
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
            text = new TextView(context);
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
        final int dp4 = text.dp(4);
        text.setPadding(dp4, dp4, dp4, dp4);
        text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return text;
    }
}
