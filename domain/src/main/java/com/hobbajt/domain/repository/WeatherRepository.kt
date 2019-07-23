package com.hobbajt.domain.repository

import com.hobbajt.domain.model.HourlyWeather
import com.hobbajt.domain.model.Weather
import io.reactivex.Single

interface WeatherRepository {

    fun dailyWeather(cityId: String): Single<List<Weather>>

    fun hourlyWeather(cityId: String): Single<List<HourlyWeather>>
}