package com.hobbajt.domain.di

import com.hobbajt.core.schedulerprovider.SchedulerProvider
import com.hobbajt.domain.repository.CityRepository
import com.hobbajt.domain.repository.WeatherRepository
import com.hobbajt.domain.usecases.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Singleton
    @Provides
    fun loadHourlyWeatherUseCase(weatherRepository: WeatherRepository, schedulerProvider: SchedulerProvider) =
        LoadHourlyWeatherUseCase(weatherRepository, schedulerProvider)

    @Singleton
    @Provides
    fun loadDailyWeatherUseCase(weatherRepository: WeatherRepository, schedulerProvider: SchedulerProvider) =
        LoadDailyWeatherUseCase(weatherRepository, schedulerProvider)

    @Singleton
    @Provides
    fun loadHistoryCitiesUseCase(cityRepository: CityRepository) =
        LoadHistoryCitiesUseCase(cityRepository)

    @Singleton
    @Provides
    fun saveCityUseCase(cityRepository: CityRepository, schedulerProvider: SchedulerProvider) =
        SaveCityUseCase(cityRepository, schedulerProvider)

    @Singleton
    @Provides
    fun searchCityUseCase(cityRepository: CityRepository, schedulerProvider: SchedulerProvider) =
        SearchCityUseCase(cityRepository, schedulerProvider)
}