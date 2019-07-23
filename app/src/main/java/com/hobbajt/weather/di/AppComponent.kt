package com.hobbajt.weather.di

import android.content.Context
import com.hobbajt.presentation.di.CoreModule
import com.hobbajt.domain.di.UseCaseModule
import com.hobbajt.repository.di.ApiModule
import com.hobbajt.repository.di.RepositoryModule
import com.hobbajt.weather.Application
import com.hobbajt.weather.di.contributors.ActivityInjectBuilder
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityInjectBuilder::class,
        RepositoryModule::class,
        ViewModelModule::class,
        ApiModule::class,
        UseCaseModule::class,
        RepositoryModule::class,
        CoreModule::class
    ]
)
interface AppComponent : AndroidInjector<Application> {

    @Component.Factory
    interface Factory {
        fun application(@BindsInstance applicationContext: Context): AppComponent
    }
}