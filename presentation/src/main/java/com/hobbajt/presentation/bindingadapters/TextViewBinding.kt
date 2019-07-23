package com.hobbajt.presentation.bindingadapters

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.hobbajt.core.R
import com.hobbajt.core.util.TimeFormat
import com.hobbajt.core.util.toText
import com.hobbajt.domain.model.Temperature
import com.hobbajt.domain.model.TemperatureCategory
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

    @JvmStatic
    @BindingAdapter("temperature")
    fun temperature(view: TextView, temperature: Temperature?) {
        temperature?.let {
            view.text = it.textValue()
            val textColor = when (it.category) {
                TemperatureCategory.LOW -> R.color.blue
                TemperatureCategory.MEDIUM -> R.color.black
                TemperatureCategory.HIGH -> R.color.red
            }
            view.setTextColor(ContextCompat.getColor(view.context, textColor))
        }
    }
}