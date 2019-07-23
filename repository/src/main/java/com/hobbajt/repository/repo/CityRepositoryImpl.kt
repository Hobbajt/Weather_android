package com.hobbajt.repository.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.hobbajt.domain.model.City
import com.hobbajt.domain.repository.CityRepository
import com.hobbajt.repository.local.SearchCityStorage
import com.hobbajt.repository.mappers.CityMapper
import com.hobbajt.repository.mappers.SearchCityMapper
import com.hobbajt.repository.remote.Api
import io.reactivex.Completable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val api: Api,
    private val searchCityStorage: SearchCityStorage
) : CityRepository {

    override fun searchCity(text: String): Single<List<City>> {
        Timber.d("Searching city by text: %s", text)
        return api.searchCity(text)
            .toObservable()
            .flatMapIterable { it }
            .map { CityMapper.mapRepositoryToDomain(it) }
            .toList()
    }

    override fun addSearchCity(city: City): Completable {
        Timber.d("Adding city to local storage")
        return searchCityStorage.searchCityDao()
            .addSearchCity(SearchCityMapper.mapDomainToRepository(city))
    }

    override fun loadSearchHistoryCities(): LiveData<List<City>> {
        Timber.d("Loading search history cities")
        return Transformations.map(searchCityStorage.searchCityDao().searchCities()) { searchCities ->
            searchCities.map { SearchCityMapper.mapRepositoryToDomain(it) }
        }
    }
}