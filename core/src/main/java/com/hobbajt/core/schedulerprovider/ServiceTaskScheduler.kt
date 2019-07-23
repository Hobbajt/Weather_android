package com.hobbajt.core.schedulerprovider

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber

class ServiceTaskScheduler(private val schedulerProvider: SchedulerProvider) {
    companion object {
        private const val UNHANDLED_ERROR_MESSAGE = "Unhandled error."
    }

    private val disposable = CompositeDisposable()

    fun schedule(
        completable: Completable,
        errorFun: ((error: Throwable) -> Unit)? = null,
        resultFun: (() -> Unit)
    ) {
        disposable += completable
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onError = { handleError(errorFun, it) },
                onComplete = { resultFun() }
            )
    }

    fun <T : Any> schedule(
        observable: Observable<T>,
        errorFun: ((error: Throwable) -> Unit)? = null,
        loadingFun: (() -> Unit)? = null,
        resultFun: ((result: T) -> Unit)
    ) {
        disposable += observable
            .doOnSubscribe { loadingFun?.invoke() }
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onError = { handleError(errorFun, it) },
                onNext = { resultFun(it) }
            )
    }

    fun <T : Any> schedule(
        single: Single<T>,
        errorFun: ((error: Throwable) -> Unit)? = null,
        loadingFun: (() -> Unit)? = null,
        resultFun: ((result: T) -> Unit)
    ) {
        disposable += single
            .doOnSubscribe { loadingFun?.invoke() }
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onError = { handleError(errorFun, it) },
                onSuccess = { resultFun(it) }
            )
    }

    fun <T : Any> schedule(
        maybe: Maybe<T>,
        errorFun: ((error: Throwable) -> Unit)? = null,
        resultFun: ((result: T) -> Unit)
    ) {
        disposable += maybe
            .observeOn(schedulerProvider.ui())
            .subscribeBy(
                onError = { handleError(errorFun, it) },
                onSuccess = { resultFun(it) }
            )
    }

    private fun handleError(errorFun: ((error: Throwable) -> Unit)?, it: Throwable) {
        errorFun?.invoke(it) ?: Timber.e(it,
            UNHANDLED_ERROR_MESSAGE
        )
    }

    operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
        add(disposable)
    }

    fun dispose() {
        disposable.dispose()
    }
}