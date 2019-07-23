package com.hobbajt.repository.mappers

import com.hobbajt.domain.model.City
import com.hobbajt.repository.model.City as RepoCity

object CityMapper {
    fun mapRepositoryToDomain(city: RepoCity): City {
        return City(
            id = city.id,
            name = city.localizedName,
            country = city.country.LocalizedName
        )
    }
}