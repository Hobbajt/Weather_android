package com.hobbajt.repository.mappers

import com.hobbajt.domain.model.City
import org.threeten.bp.Instant
import com.hobbajt.repository.model.SearchCity as SearchCityRepo

object SearchCityMapper {

    fun mapRepositoryToDomain(searchCity: SearchCityRepo): City {
        return City(
            id = searchCity.id,
            name = searchCity.name,
            country = searchCity.country
        )
    }

    fun mapDomainToRepository(city: City): SearchCityRepo {
        return SearchCityRepo(
            id = city.id,
            name = city.name,
            country = city.country,
            timestamp = Instant.now().epochSecond
        )
    }
}