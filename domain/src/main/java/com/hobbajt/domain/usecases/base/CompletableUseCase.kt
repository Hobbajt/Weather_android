package com.hobbajt.domain.usecases.base

import com.hobbajt.core.schedulerprovider.SchedulerProvider
import io.reactivex.Completable

abstract class CompletableUseCase<Params>(private val schedulerProvider: SchedulerProvider) {
    fun execute(params: Params): Completable {
        return buildUseCaseCompletable(params)
            .retry(3)
            .subscribeOn(schedulerProvider.io())
    }

    internal abstract fun buildUseCaseCompletable(params: Params): Completable
}