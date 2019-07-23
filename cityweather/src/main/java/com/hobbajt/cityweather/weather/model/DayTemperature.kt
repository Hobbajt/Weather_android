package com.hobbajt.cityweather.weather.model

import androidx.annotation.DrawableRes

data class DayTemperature(
    val dayName: String?,
    val maxTemperature: String,
    val minTemperature: String,
    @DrawableRes val iconId: Int
)