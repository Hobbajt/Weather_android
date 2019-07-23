package com.hobbajt.weather

import com.hobbajt.repository.local.SearchCityStorage
import com.hobbajt.weather.di.DaggerAppComponent
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class Application : DaggerApplication() {
    override fun onCreate() {
        super.onCreate()
        initThreeTen()
        initTimber()
        initDatabase()
    }

    private fun initThreeTen() {
        AndroidThreeTen.init(this)
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initDatabase() {
        SearchCityStorage.getDatabase(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().application(this)
    }
}