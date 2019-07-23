package com.hobbajt.domain.usecases

import com.hobbajt.core.schedulerprovider.SchedulerProvider
import com.hobbajt.domain.model.HourlyWeather
import com.hobbajt.domain.repository.WeatherRepository
import com.hobbajt.domain.usecases.base.SingleUseCase
import io.reactivex.Single

class LoadHourlyWeatherUseCase(
    private val weatherRepository: WeatherRepository,
    schedulerProvider: SchedulerProvider
) : SingleUseCase<List<HourlyWeather>, LoadHourlyWeatherUseCase.Params>(schedulerProvider) {

    override fun create(params: Params): Single<List<HourlyWeather>> {
        return weatherRepository.hourlyWeather(params.cityId)
    }

    data class Params(val cityId: String)
}