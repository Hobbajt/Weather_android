package com.hobbajt.cityweather.weather

import com.hobbajt.cityweather.R
import com.hobbajt.domain.model.Icon

object WeatherIconFactory {
    fun getImageId(icon: Icon): Int {
        return when (icon) {
            Icon.Sun -> R.drawable.ic_sun
            Icon.SunAndClouds -> R.drawable.ic_cloud_sun
            Icon.Clouds -> R.drawable.ic_cloud
            Icon.Fog -> R.drawable.ic_fog
            Icon.CloudsAndRain -> R.drawable.ic_cloud_rain
            Icon.CloudsAndSnow -> R.drawable.ic_cloud_snow
            Icon.SunAndCloudsAndRain -> R.drawable.ic_cloud_sun_rain
            Icon.CloudsAndRainAndStorm -> R.drawable.ic_cloud_sun_storm
            Icon.None -> R.drawable.ic_cloud_error
        }
    }
}