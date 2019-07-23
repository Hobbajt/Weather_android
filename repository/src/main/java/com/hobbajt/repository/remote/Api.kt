package com.hobbajt.repository.remote

import com.hobbajt.repository.model.City
import com.hobbajt.repository.model.HourlyWeather
import com.hobbajt.repository.model.Weather
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {
    companion object {
        private const val LONG_HOUR_WEATHER = "/forecasts/v1/hourly/12hour/{placeId}?details=true"
        private const val DAILY_WEATHER = "/forecasts/v1/daily/5day/{placeId}?details=true"
        private const val SEARCH_CITY = "/locations/v1/cities/autocomplete"
    }

    @GET(LONG_HOUR_WEATHER)
    fun longHourWeather(
        @Path("placeId") cityId: String,
        @Query("details") loadDetails: Boolean = true,
        @Query("metric") loadMetricValues: Boolean = true
    ): Single<List<HourlyWeather>>

    @GET(SEARCH_CITY)
    fun searchCity(@Query("q") cityName: String): Single<List<City>>

    @GET(DAILY_WEATHER)
    fun dailyWeather(
        @Path("placeId") cityId: String,
        @Query("details") loadDetails: Boolean = true,
        @Query("metric") loadMetricValues: Boolean = true
    ): Single<Weather>
}