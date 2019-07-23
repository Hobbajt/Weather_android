package com.hobbajt.cityweather.citylist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hobbajt.domain.model.City
import com.hobbajt.domain.model.HourlyWeather

class SelectedCityVM : ViewModel() {
    val selectedCityWeather = MutableLiveData<Pair<City, List<HourlyWeather>>>()

    fun select(selectedCity: City, selectedHourlyWeather: List<HourlyWeather>) {
        selectedCityWeather.value = Pair(selectedCity, selectedHourlyWeather)
    }
}