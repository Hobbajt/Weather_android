package com.hobbajt.repository.di

import android.content.Context
import com.hobbajt.domain.repository.CityRepository
import com.hobbajt.domain.repository.WeatherRepository
import com.hobbajt.repository.local.SearchCityStorage
import com.hobbajt.repository.remote.Api
import com.hobbajt.repository.repo.CityRepositoryImpl
import com.hobbajt.repository.repo.WeatherRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class RepositoryModule {

    @Singleton
    @Provides
    fun searchCityStorage(context: Context) = SearchCityStorage.getDatabase(context)

    @Singleton
    @Provides
    fun cityRepository(api: Api, searchCityStorage: SearchCityStorage): CityRepository = CityRepositoryImpl(api, searchCityStorage)

    @Singleton
    @Provides
    fun weatherRepository(api: Api): WeatherRepository = WeatherRepositoryImpl(api)

}