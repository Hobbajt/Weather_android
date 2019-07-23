package com.hobbajt.presentation.bindingadapters

import android.view.View
import androidx.databinding.BindingAdapter

object ViewBinding {

    @JvmStatic
    @BindingAdapter("isVisible")
    fun isVisible(view: View, isVisible: Boolean?) {
        if (isVisible == true) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}