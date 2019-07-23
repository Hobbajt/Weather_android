package com.hobbajt.presentation.base

import androidx.lifecycle.ViewModel
import com.hobbajt.core.schedulerprovider.SchedulerProvider
import com.hobbajt.core.schedulerprovider.ServiceTaskScheduler
import io.reactivex.Observable
import io.reactivex.Single

abstract class SchedulerViewModel(schedulerProvider: SchedulerProvider) : ViewModel() {
    private val serviceTaskScheduler =
        ServiceTaskScheduler(schedulerProvider)

    override fun onCleared() {
        super.onCleared()
        serviceTaskScheduler.dispose()
    }

    fun <T : Any> schedule(
        observable: Observable<T>,
        errorFun: ((error: Throwable) -> Unit)? = null,
        loadingFun: (() -> Unit)? = null,
        nextFun: ((result: T) -> Unit)
    ) {
        serviceTaskScheduler.schedule(observable, errorFun, loadingFun, nextFun)
    }

    fun <T : Any> schedule(
        single: Single<T>,
        errorFun: ((error: Throwable) -> Unit)? = null,
        loadingFun: (() -> Unit)? = null,
        resultFun: ((result: T) -> Unit)
    ) {
        serviceTaskScheduler.schedule(single, errorFun, loadingFun, resultFun)
    }
}