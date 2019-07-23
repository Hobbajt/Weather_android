package com.hobbajt.weather.di.contributors

import com.hobbajt.weather.MainActivity
import com.hobbajt.weather.di.scopes.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityInjectBuilder {

    @ActivityScope
    @ContributesAndroidInjector(modules = [FragmentInjectBuilder::class])
    fun mainActivity(): MainActivity

}