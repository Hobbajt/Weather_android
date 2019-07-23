package com.hobbajt.cityweather.weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.hobbajt.core.schedulerprovider.TestSchedulerProvider
import com.hobbajt.domain.model.*
import com.hobbajt.domain.usecases.LoadDailyWeatherUseCase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

@RunWith(MockitoJUnitRunner::class)
class WeatherVMTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var loadDailyWeatherUseCase: LoadDailyWeatherUseCase

    private lateinit var vm: WeatherVM
    private lateinit var lifecycle: LifecycleRegistry

    private val daysWeather = listOf(
        Weather(
            date = ZonedDateTime.of(2019, 8, 7, 19, 18, 41, 523, ZoneId.of("UTC")),
            icon = Icon.Clouds,
            maxTemperature = Temperature(15F, "C", TemperatureCategory.MEDIUM),
            indexUv = 4,
            airQuality = "Good",
            cloudCover = WeatherProperty("%", 61F),
            wind = WeatherProperty("km/h", 16F),
            rain = WeatherProperty("mm", 32.3F),
            description = "Sample description",
            minTemperature = Temperature(4F, "C", TemperatureCategory.LOW)
        )
    )

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
        lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        vm = WeatherVM(loadDailyWeatherUseCase,
            TestSchedulerProvider(Schedulers.trampoline())
        )
    }

    @Test
    fun onCitySelected() {
        `when`(loadDailyWeatherUseCase.execute(LoadDailyWeatherUseCase.Params(city.id))).thenReturn(
            Single.just(
                daysWeather
            )
        )
        assertNull(vm.getCity().value)
        vm.onCitySelected(Pair(city, hourlyWeather))
        assertEquals(city, vm.getCity().value)
        verify(loadDailyWeatherUseCase).execute(LoadDailyWeatherUseCase.Params(city.id))
        assertEquals(true, vm.getIsLoadingDailyWeatherFinished().value)
        assertEquals(daysWeather, vm.getDaysWeather().value)
        assertEquals(daysWeather.firstOrNull(), vm.getTodayWeather().value)
    }

    @Test(expected = RuntimeException::class)
    fun onCitySelectedError() {
        assertNull(vm.getCity().value)
        vm.onCitySelected(Pair(city, hourlyWeather))
        assertEquals(city, vm.getCity().value)
        verify(loadDailyWeatherUseCase).execute(LoadDailyWeatherUseCase.Params(city.id))
        assertEquals(false, vm.getIsLoadingDailyWeatherFinished().value)
        assertEquals(null, vm.getDaysWeather().value)
        assertEquals(null, vm.getTodayWeather().value)
    }
}