package com.hobbajt.domain.usecases

import androidx.lifecycle.LiveData
import com.hobbajt.domain.model.City
import com.hobbajt.domain.repository.CityRepository
import com.hobbajt.domain.usecases.base.LiveDataUseCase
import javax.inject.Inject

class LoadHistoryCitiesUseCase @Inject constructor(
    private val cityRepository: CityRepository
) : LiveDataUseCase<List<City>, Unit>() {

    override fun create(params: Unit): LiveData<List<City>> {
        return cityRepository.loadSearchHistoryCities()
    }
}