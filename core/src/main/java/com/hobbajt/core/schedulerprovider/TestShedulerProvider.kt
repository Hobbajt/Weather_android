package com.hobbajt.core.schedulerprovider

import io.reactivex.Scheduler

class TestSchedulerProvider(private val testScheduler: Scheduler) :
    SchedulerProvider {
    override fun ui(): Scheduler = testScheduler

    override fun computation(): Scheduler = testScheduler

    override fun io(): Scheduler = testScheduler
}