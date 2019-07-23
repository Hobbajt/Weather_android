package com.hobbajt.weather

import com.hobbajt.core.schedulerprovider.SchedulerProvider
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class TestSchedulerProvider(private val testScheduler: TestScheduler) :
    SchedulerProvider {
    override fun ui(): Scheduler = testScheduler

    override fun computation(): Scheduler = testScheduler

    override fun io(): Scheduler = testScheduler
}