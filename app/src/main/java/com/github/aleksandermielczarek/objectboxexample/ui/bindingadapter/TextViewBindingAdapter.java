package com.github.aleksandermielczarek.objectboxexample.ui.bindingadapter;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.widget.TextView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.format.DateTimeFormatter;

/**
 * Created by Aleksander Mielczarek on 11.03.2017.
 */

public class TextViewBindingAdapter {

    private TextViewBindingAdapter() {

    }

    @BindingAdapter("date")
    public static void setDate(TextView textView, LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String formattedDate = formatter.format(date);
        textView.setText(formattedDate);
    }

    @BindingAdapter("bold")
    public static void setBold(TextView textView, boolean bold) {
        if (bold) {
            textView.setTypeface(null, Typeface.BOLD);
        } else {
            textView.setTypeface(null, Typeface.NORMAL);
        }
    }
}
