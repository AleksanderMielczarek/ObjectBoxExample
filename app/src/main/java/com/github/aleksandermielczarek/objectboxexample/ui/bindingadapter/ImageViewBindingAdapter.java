package com.github.aleksandermielczarek.objectboxexample.ui.bindingadapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import com.github.aleksandermielczarek.objectboxexample.R;

/**
 * Created by Aleksander Mielczarek on 11.03.2017.
 */

public class ImageViewBindingAdapter {

    private ImageViewBindingAdapter() {

    }

    @BindingAdapter({"selection"})
    public static void setSelection(ImageView imageView, boolean selection) {
        Context context = imageView.getContext();
        int[] attrs = {android.R.attr.textColorPrimary, android.R.attr.textColorSecondary};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int selectedColor = ta.getResourceId(0, R.color.colorNotificationRead);
        int notSelectedColor = ta.getResourceId(1, R.color.colorNotificationNotRead);
        ta.recycle();
        Drawable tintedDrawable = DrawableCompat.wrap(imageView.getDrawable().mutate());
        DrawableCompat.setTint(tintedDrawable, ContextCompat.getColor(context, selection ? selectedColor : notSelectedColor));
        imageView.setImageDrawable(tintedDrawable);
    }
}