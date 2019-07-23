package com.hobbajt.domain.model

import org.threeten.bp.ZonedDateTime
import kotlin.math.roundToInt

data class Weather(
    val date: ZonedDateTime,
    val icon: Icon,
    val maxTemperature: Temperature,
    val minTemperature: Temperature,
    val description: String,
    val rain: WeatherProperty,
    val wind: WeatherProperty,
    val indexUv: Int,
    val cloudCover: WeatherProperty,
    val airQuality: String
)

data class HourlyWeather(
    val date: ZonedDateTime,
    val temperature: Temperature,
    val description: String,
    val rain: WeatherProperty,
    val wind: WeatherProperty,
    val visibility: WeatherProperty,
    val icon: Icon,
    val snow: WeatherProperty
)

data class WeatherProperty(
    val unit: String,
    val value: Float
)

data class Temperature(
    val value: Float,
    val unit: String,
    val category: TemperatureCategory
) {
    fun textValue(): String {
        return "${value.roundToInt()}°"
    }
}

enum class TemperatureCategory {
    LOW,
    MEDIUM,
    HIGH;

    companion object {
        fun find(temperature: Float): TemperatureCategory {
            return when {
                temperature < 10F -> LOW
                temperature > 20F -> HIGH
                else -> MEDIUM
            }
        }
    }
}

/**
 * Accuweather provides an API with very specific icons that don't match the appearance of the application.
 * The Documentation says that app shouldn't use direct links to them.
 * Accuweather icon ids are grouped and assigned to local app's icons.
 * @see <a href="https://apidev.accuweather.com/developers/weatherIcons">Documentation</a>
 */
enum class Icon(val id: Int, val typeIds: IntArray) {
    Sun(0, intArrayOf(1, 2, 3, 4)),
    SunAndClouds(1, intArrayOf(5, 6, 20, 21, 23)),
    Clouds(2, intArrayOf(7, 8, 19)),
    Fog(3, intArrayOf(11)),
    CloudsAndRain(4, intArrayOf(12, 18, 26)),
    CloudsAndSnow(5, intArrayOf(22, 25, 29)),
    SunAndCloudsAndRain(6, intArrayOf(13, 14)),
    CloudsAndRainAndStorm(7, intArrayOf(15, 16, 17)),
    None(8, intArrayOf());

    companion object {
        fun find(typeId: Int): Icon {
            return values().firstOrNull { it.typeIds.contains(typeId) } ?: None
        }
    }
}