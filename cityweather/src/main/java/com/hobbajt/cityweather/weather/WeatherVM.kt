package com.hobbajt.cityweather.weather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hobbajt.cityweather.R
import com.hobbajt.cityweather.weather.model.ChartValue
import com.hobbajt.cityweather.weather.model.DayTemperature
import com.hobbajt.cityweather.weather.model.WeatherDetail
import com.hobbajt.core.schedulerprovider.SchedulerProvider
import com.hobbajt.core.util.TimeFormat
import com.hobbajt.core.util.toText
import com.hobbajt.domain.model.City
import com.hobbajt.domain.model.HourlyWeather
import com.hobbajt.domain.model.Weather
import com.hobbajt.domain.usecases.LoadDailyWeatherUseCase
import com.hobbajt.presentation.base.SchedulerViewModel
import com.hobbajt.presentation.base.Toolbar
import com.hobbajt.presentation.base.ToolbarOwner
import javax.inject.Inject

class WeatherVM @Inject constructor(
    private val loadDailyWeatherUseCase: LoadDailyWeatherUseCase,
    schedulerProvider: SchedulerProvider
) : SchedulerViewModel(schedulerProvider), ToolbarOwner {

    override var toolbar = MutableLiveData<Toolbar>().apply {
        value = Toolbar(
            titleId = R.string.weather_title,
            colorId = R.color.lightOrange
        )
    }

    private val city = MutableLiveData<City>()

    private val daysWeather = MutableLiveData<List<Weather>>()
    private val dayTemperatures = MutableLiveData<List<DayTemperature>>()
    private val todayWeather = MutableLiveData<Weather>()
    private val isLoadingDailyWeatherFinished = MutableLiveData(false)

    private val hourlyTemperatures = MutableLiveData<List<ChartValue>>()
    private val currentWeather = MutableLiveData<HourlyWeather>()

    private val wind = MutableLiveData<WeatherDetail>()
    private val indexUv = MutableLiveData<WeatherDetail>()
    private val airQuality = MutableLiveData<WeatherDetail>()

    fun getCity(): LiveData<City> = city
    fun getDaysWeather(): LiveData<List<Weather>> = daysWeather
    fun getDayTemperatures(): LiveData<List<DayTemperature>> = dayTemperatures
    fun getTodayWeather(): LiveData<Weather> = todayWeather
    fun getIsLoadingDailyWeatherFinished(): LiveData<Boolean> = isLoadingDailyWeatherFinished
    fun getHourlyTemperatures(): LiveData<List<ChartValue>> = hourlyTemperatures
    fun getCurrentWeather(): LiveData<HourlyWeather> = currentWeather

    fun getWind(): LiveData<WeatherDetail> = wind
    fun getIndexUv(): LiveData<WeatherDetail> = indexUv
    fun getAirQuality(): LiveData<WeatherDetail> = airQuality


    fun onCitySelected(selectedCityAndWeather: Pair<City, List<HourlyWeather>>) {
        city.value = selectedCityAndWeather.first
        currentWeather.value = selectedCityAndWeather.second.first()
        hourlyTemperatures.value = selectedCityAndWeather.second.map {
            ChartValue(
                value = it.temperature.value,
                valueTitle = it.temperature.textValue(),
                title = it.date.toText(TimeFormat.Hour).toLowerCase()
            )
        }

        loadDailyWeather()
    }

    private fun loadDailyWeather() {
        city.value?.id?.let { placeId ->
            schedule(loadDailyWeatherUseCase.execute(LoadDailyWeatherUseCase.Params(placeId)),
                resultFun = {
                    isLoadingDailyWeatherFinished.value = true
                    daysWeather.value = it

                    dayTemperatures.value = it.map { weather -> createDayTemperatureItem(weather) }
                        // Add 8 additional locked days, to achieve scrollable effect. The free API version allows to load temperature for up to 5 days.
                        .plus(IntRange(0, 7).map { createLockedTemperatureItem() })

                    it.firstOrNull()?.let { weather ->
                        todayWeather.value = weather
                        createWeatherDetails(weather)
                    }
                },
                errorFun = {
                    isLoadingDailyWeatherFinished.value = false
                }
            )
        }
    }

    private fun createLockedTemperatureItem(): DayTemperature {
        return DayTemperature(
            dayName = null,
            maxTemperature = "-",
            minTemperature = "-",
            iconId = R.drawable.ic_cloud_error
        )
    }

    private fun createDayTemperatureItem(it: Weather): DayTemperature {
        return DayTemperature(
            dayName = it.date.toText(TimeFormat.DayName).capitalize(),
            maxTemperature = it.maxTemperature.textValue(),
            minTemperature = it.minTemperature.textValue(),
            iconId = WeatherIconFactory.getImageId(it.icon)
        )
    }

    private fun createWeatherDetails(weather: Weather) {
        wind.value = WeatherDetail(
            value = "${weather.wind.value} ${weather.wind.unit}",
            nameId = R.string.weather_wind_title,
            iconId = R.drawable.ic_wind
        )
        indexUv.value = WeatherDetail(
            value = "${weather.indexUv}",
            nameId = R.string.weather_uv_index_title,
            iconId = R.drawable.ic_sun
        )
        airQuality.value = WeatherDetail(
            value = weather.airQuality,
            nameId = R.string.weather_air_quality_title,
            iconId = R.drawable.ic_air_quality
        )
    }
}