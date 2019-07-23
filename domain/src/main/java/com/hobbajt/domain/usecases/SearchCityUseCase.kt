package com.hobbajt.domain.usecases

import com.hobbajt.core.schedulerprovider.SchedulerProvider
import com.hobbajt.domain.model.City
import com.hobbajt.domain.repository.CityRepository
import com.hobbajt.domain.usecases.base.SingleUseCase
import io.reactivex.Single
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val cityRepository: CityRepository,
    schedulerProvider: SchedulerProvider
) : SingleUseCase<List<City>, SearchCityUseCase.Params>(schedulerProvider) {

    override fun create(params: Params): Single<List<City>> {
        return cityRepository.searchCity(params.text)
    }

    data class Params(val text: String)
}