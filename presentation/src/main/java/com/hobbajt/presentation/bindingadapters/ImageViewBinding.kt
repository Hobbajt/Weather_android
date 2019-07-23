package com.hobbajt.presentation.bindingadapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter

object ImageViewBinding {

    @JvmStatic
    @BindingAdapter("android:src")
    fun idSrc(imageView: ImageView, drawableId: Int?) {
        drawableId?.let {
            imageView.setImageResource(it)
        }
    }
}