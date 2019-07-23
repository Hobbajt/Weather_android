package com.hobbajt.domain.usecases

import com.hobbajt.core.schedulerprovider.SchedulerProvider
import com.hobbajt.domain.model.City
import com.hobbajt.domain.repository.CityRepository
import com.hobbajt.domain.usecases.base.CompletableUseCase
import io.reactivex.Completable
import javax.inject.Inject

class SaveCityUseCase @Inject constructor(
    private val cityRepository: CityRepository,
    schedulerProvider: SchedulerProvider
) : CompletableUseCase<SaveCityUseCase.Params>(schedulerProvider) {

    override fun buildUseCaseCompletable(params: Params): Completable {
        return cityRepository.addSearchCity(params.city)
    }

    data class Params(val city: City)
}