package com.hobbajt.domain.usecases.base

import androidx.lifecycle.LiveData

abstract class LiveDataUseCase<T, Params> {
    fun execute(params: Params): LiveData<T> {
        return create(params)
    }

    internal abstract fun create(params: Params): LiveData<T>
}