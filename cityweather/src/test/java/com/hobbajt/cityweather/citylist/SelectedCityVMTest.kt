package com.hobbajt.cityweather.citylist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.hobbajt.domain.model.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

@RunWith(MockitoJUnitRunner::class)
class SelectedCityVMTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var vm: SelectedCityVM
    private lateinit var lifecycle: LifecycleRegistry

    private val hourlyWeather = listOf(
        HourlyWeather(
            date = ZonedDateTime.of(2019, 8, 7, 19, 18, 41, 523, ZoneId.of("UTC")),
            icon = Icon.Clouds,

            wind = WeatherProperty("km/h", 16F),
            rain = WeatherProperty("mm", 32.3F),
            description = "Sample description",
            visibility = WeatherProperty("m", 482F),
            snow = WeatherProperty("mm", 12F),
            temperature = Temperature(15F, "C", TemperatureCategory.MEDIUM)
        )
    )

    private val city = City(
        id = "1234",
        name = "Warsaw",
        country = "Poland"
    )

    @Before
    fun setup() {
        lifecycle = LifecycleRegistry(Mockito.mock(LifecycleOwner::class.java))
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        vm = SelectedCityVM()
    }

    @Test
    fun select() {
        assertNull(vm.selectedCityWeather.value)
        vm.select(city, hourlyWeather)
        assertEquals(Pair(city, hourlyWeather), vm.selectedCityWeather.value)
    }
}