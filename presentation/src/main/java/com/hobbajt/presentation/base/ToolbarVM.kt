package com.hobbajt.presentation.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ToolbarVM : ViewModel() {
    val toolbar = MutableLiveData<Toolbar>()

    fun updateToolbar(newToolbar: Toolbar) {
        toolbar.value = newToolbar
    }
}

data class Toolbar(
    val titleId: Int,
    val colorId: Int
)

interface ToolbarOwner {
    val toolbar: MutableLiveData<Toolbar>
}