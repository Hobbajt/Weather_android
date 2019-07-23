package com.hobbajt.domain.usecases

import com.hobbajt.core.schedulerprovider.SchedulerProvider
import com.hobbajt.domain.model.Weather
import com.hobbajt.domain.repository.WeatherRepository
import com.hobbajt.domain.usecases.base.SingleUseCase
import io.reactivex.Single

class LoadDailyWeatherUseCase(
    private val weatherRepository: WeatherRepository,
    schedulerProvider: SchedulerProvider
) : SingleUseCase<List<Weather>, LoadDailyWeatherUseCase.Params>(schedulerProvider) {

    override fun create(params: Params): Single<List<Weather>> {
        return weatherRepository.dailyWeather(params.placeId)
    }

    data class Params(val placeId: String)
}