package com.hobbajt.repository.model

import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime

data class HourlyWeather(
    @SerializedName("DateTime")
    val date: ZonedDateTime,
    @SerializedName("WeatherIcon")
    val iconId: Int,
    @SerializedName("Rain")
    val rain: WeatherProperty,
    @SerializedName("Wind")
    val wind: Wind,
    @SerializedName("RelativeHumidity")
    val humidity: Int,
    @SerializedName("IconPhrase")
    val description: String,
    @SerializedName("Temperature")
    val temperature: WeatherProperty,
    @SerializedName("Visibility")
    val visibility: WeatherProperty,
    @SerializedName("Snow")
    val snow: WeatherProperty
)