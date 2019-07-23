package com.hobbajt.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

/**
 * Custom {@link MutableLiveData}, that should be used for actions and events.
 * It can have only one observer at the same time. It works like PublishSubject as
 * opposed to standard MutableLiveData, which works like BehaviourSubject.
 * @see MutableLiveData
 */
class ActionLiveData<T> : MutableLiveData<T>() {

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        removeObservers(owner)

        super.observe(owner, Observer { data ->
            if (data == null) {
                return@Observer
            }
            observer.onChanged(data)
            value = null
        })
    }

    @MainThread
    fun sendAction(data: T) {
        value = data
    }
}