package com.hobbajt.presentation.bindingadapters

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.hobbajt.core.util.TimeFormat
import com.hobbajt.core.util.toText
import org.threeten.bp.ZonedDateTime

object TextViewBinding {

    @JvmStatic
    @BindingAdapter("android:text")
    fun resText(view: TextView, @StringRes id: Int?) {
        id?.let {
            view.text = view.context?.getText(it)
        }
    }

    @JvmStatic
    @BindingAdapter("android:text")
    fun dateText(view: TextView, date: ZonedDateTime?) {
        date?.let {
            view.text = date.toText(TimeFormat.Full)
        }
    }
}