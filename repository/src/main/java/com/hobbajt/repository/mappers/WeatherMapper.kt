package com.hobbajt.repository.mappers

import com.hobbajt.domain.model.*
import com.hobbajt.repository.model.HourlyWeather as RepoHourlyWeather
import com.hobbajt.repository.model.Weather as RepoWeather
import com.hobbajt.repository.model.WeatherProperty as RepoWeatherProperty

object WeatherMapper {
    private const val AIR_QUALITY_PROPERTY_NAME = "AirQuality"
    private const val UV_INDEX_PROPERTY_NAME = "UVIndex"
    private const val PERCENT_SIGN = "%"

    fun mapRepositoryToDomain(weather: RepoWeather): List<Weather> {
        return weather.dailyForecasts.map {
            Weather(
                description = weather.headline.description,
                date = it.date,
                icon = Icon.find(it.day.iconId),
                minTemperature = TemperatureMapper.mapRepositoryToDomain(it.temperature.minimum),
                maxTemperature = TemperatureMapper.mapRepositoryToDomain(it.temperature.maximum),
                rain = WeatherPropertyMapper.mapRepositoryToDomain(it.day.rain),
                wind = WeatherPropertyMapper.mapRepositoryToDomain(it.day.wind.speed),
                cloudCover = WeatherProperty(PERCENT_SIGN, it.day.cloudCover.toFloat()),
                airQuality = it.airQualityComponents.find { component -> component.name == AIR_QUALITY_PROPERTY_NAME }?.category ?: "",
                indexUv = it.airQualityComponents.find { component -> component.name == UV_INDEX_PROPERTY_NAME }?.value ?: 0
            )
        }
    }

    fun mapRepositoryToDomain(weather: RepoHourlyWeather): HourlyWeather {
        return HourlyWeather(
            description = weather.description,
            date = weather.date,
            temperature = TemperatureMapper.mapRepositoryToDomain(weather.temperature),
            rain = WeatherPropertyMapper.mapRepositoryToDomain(weather.rain),
            wind = WeatherPropertyMapper.mapRepositoryToDomain(weather.wind.speed),
            icon = Icon.find(weather.iconId),
            visibility = WeatherPropertyMapper.mapRepositoryToDomain(weather.visibility),
            snow = WeatherPropertyMapper.mapRepositoryToDomain(weather.snow)
        )
    }
}

object WeatherPropertyMapper {
    fun mapRepositoryToDomain(weatherProperty: RepoWeatherProperty): WeatherProperty {
        return WeatherProperty(
            unit = weatherProperty.unit,
            value = weatherProperty.value
        )
    }
}

object TemperatureMapper {
    fun mapRepositoryToDomain(weatherProperty: RepoWeatherProperty): Temperature {
        return Temperature(
            unit = weatherProperty.unit,
            value = weatherProperty.value,
            category = TemperatureCategory.find(weatherProperty.value)
        )
    }
}