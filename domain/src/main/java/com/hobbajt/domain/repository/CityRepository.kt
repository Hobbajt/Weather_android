package com.hobbajt.domain.repository

import androidx.lifecycle.LiveData
import com.hobbajt.domain.model.City
import io.reactivex.Completable
import io.reactivex.Single

interface CityRepository {

    fun searchCity(text: String): Single<List<City>>

    fun loadSearchHistoryCities(): LiveData<List<City>>

    fun addSearchCity(city: City): Completable
}