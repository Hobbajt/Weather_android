package com.hobbajt.presentation.di

import com.hobbajt.core.schedulerprovider.AppSchedulerProvider
import com.hobbajt.core.schedulerprovider.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreModule {

    @Singleton
    @Provides
    fun schedulerProvider(): SchedulerProvider =
        AppSchedulerProvider()
}