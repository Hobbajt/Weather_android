package com.hobbajt.cityweather.weather.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class WeatherDetail(
    val value: String,
    @StringRes val nameId: Int? = null,
    @DrawableRes val iconId: Int? = null
)