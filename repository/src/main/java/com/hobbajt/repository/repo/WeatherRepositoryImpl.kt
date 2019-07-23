package com.hobbajt.repository.repo

import com.hobbajt.domain.model.HourlyWeather
import com.hobbajt.domain.model.Weather
import com.hobbajt.domain.repository.WeatherRepository
import com.hobbajt.repository.mappers.WeatherMapper
import com.hobbajt.repository.remote.Api
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: Api) : WeatherRepository {

    override fun dailyWeather(cityId: String): Single<List<Weather>> {
        Timber.d("Loading daily weather")
        return api.dailyWeather(cityId)
            .map { WeatherMapper.mapRepositoryToDomain(it) }
    }

    override fun hourlyWeather(cityId: String): Single<List<HourlyWeather>> {
        Timber.d("Loading hourly weather")
        return api.longHourWeather(cityId)
            .toObservable()
            .flatMapIterable { it }
            .map { WeatherMapper.mapRepositoryToDomain(it) }
            .toList()
    }
}