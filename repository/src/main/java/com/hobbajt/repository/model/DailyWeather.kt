package com.hobbajt.repository.model

import com.google.gson.annotations.SerializedName
import org.threeten.bp.ZonedDateTime

data class Weather(
    @SerializedName("DailyForecasts")
    val dailyForecasts: List<DailyForecast>,
    @SerializedName("Headline")
    val headline: Headline
)

data class Headline(
    @SerializedName("Text")
    val description: String
)

data class DailyForecast(
    @SerializedName("Date")
    val date: ZonedDateTime,
    @SerializedName("Temperature")
    val temperature: Temperatures,
    @SerializedName("Day")
    val day: Day,
    @SerializedName("AirAndPollen")
    val airQualityComponents: List<AirAndPollenQualityComponent>
)

data class Temperatures(
    @SerializedName("Maximum")
    val maximum: WeatherProperty,
    @SerializedName("Minimum")
    val minimum: WeatherProperty
)

data class WeatherProperty(
    @SerializedName("Unit")
    val unit: String,
    @SerializedName("Value")
    val value: Float,
    @SerializedName("UnitType")
    val unitType: Int
)

data class Day(
    @SerializedName("Icon")
    val iconId: Int,
    @SerializedName("Rain")
    val rain: WeatherProperty,
    @SerializedName("Wind")
    val wind: Wind,
    @SerializedName("Snow")
    val snow: WeatherProperty,
    @SerializedName("Ice")
    val ice: WeatherProperty,
    @SerializedName("CloudCover")
    val cloudCover: Int
)

data class Wind(
    @SerializedName("Speed")
    val speed: WeatherProperty
)

data class AirAndPollenQualityComponent(
    @SerializedName("Category")
    val category: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Value")
    val value: Int
)