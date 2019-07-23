package com.hobbajt.weather.di.contributors

import com.hobbajt.cityweather.citylist.CityListFragment
import com.hobbajt.cityweather.weather.WeatherFragment
import com.hobbajt.weather.di.scopes.FragmentScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentInjectBuilder {
    @FragmentScope
    @ContributesAndroidInjector
    fun cityListFragment(): CityListFragment

    @FragmentScope
    @ContributesAndroidInjector
    fun weatherFragment(): WeatherFragment
}