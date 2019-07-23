package com.hobbajt.domain.usecases.base

import com.hobbajt.core.schedulerprovider.SchedulerProvider
import io.reactivex.Single

abstract class SingleUseCase<T, Params>(private val schedulerProvider: SchedulerProvider) {
    fun execute(params: Params): Single<T> {
        return create(params)
            .retry(3)
            .subscribeOn(schedulerProvider.io())
    }

    internal abstract fun create(params: Params): Single<T>
}