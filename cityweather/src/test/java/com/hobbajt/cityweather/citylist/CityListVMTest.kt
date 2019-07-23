package com.hobbajt.cityweather.citylist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import com.hobbajt.core.schedulerprovider.TestSchedulerProvider
import com.hobbajt.domain.model.*
import com.hobbajt.domain.usecases.LoadHistoryCitiesUseCase
import com.hobbajt.domain.usecases.LoadHourlyWeatherUseCase
import com.hobbajt.domain.usecases.SaveCityUseCase
import com.hobbajt.domain.usecases.SearchCityUseCase
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class CityListVMTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var searchCityUseCase: SearchCityUseCase
    @Mock
    lateinit var saveCityUseCase: SaveCityUseCase
    @Mock
    lateinit var loadHistoryCitiesUseCase: LoadHistoryCitiesUseCase
    @Mock
    lateinit var loadHourlyWeatherUseCase: LoadHourlyWeatherUseCase

    private lateinit var vm: CityListVM
    private lateinit var lifecycle: LifecycleRegistry

    private val scheduler = TestScheduler()
    private val schedulerProvider = TestSchedulerProvider(scheduler)

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

    private val historyCities = listOf(
        City(
            id = "1235",
            name = "Bydgoszcz",
            country = "Poland"
        ),
        City(
            id = "1236",
            name = "Gdynia",
            country = "Poland"
        )
    )

    @Before
    fun setup() {
        `when`(loadHistoryCitiesUseCase.execute(Unit))
            .thenReturn(MutableLiveData<List<City>>().apply { value = emptyList() })

        `when`(loadHistoryCitiesUseCase.execute(Unit))
            .thenReturn(MutableLiveData<List<City>>().apply { value = historyCities })

        lifecycle = LifecycleRegistry(mock(LifecycleOwner::class.java))
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        vm = CityListVM(
            searchCityUseCase,
            saveCityUseCase,
            loadHistoryCitiesUseCase,
            loadHourlyWeatherUseCase,
            schedulerProvider
        )
    }

    @Test
    fun onEmptySearchTextChanged() {
        vm.searchText.value = ""
        vm.afterSearchTextChanged()
        assertEquals(false, vm.getIsLoadingError().value)
        assertEquals(historyCities, vm.getCities().value)
        assertEquals(true, vm.getAreHistoryCitiesDisplayed().value)
    }

    @Test
    fun onNotEmptySearchTextChanged() {
        `when`(searchCityUseCase.execute(any(SearchCityUseCase.Params::class.java)))
            .thenReturn(Single.just(listOf(city)))

        vm.getCities().observeForever {}

        assertEquals(false, vm.getIsLoading().value)
        vm.searchText.value = "Wars"
        vm.afterSearchTextChanged()
        assertEquals(false, vm.getIsLoadingError().value)
        assertEquals(true, vm.getIsLoading().value)
        scheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)
        verify(searchCityUseCase).execute(SearchCityUseCase.Params("Wars"))
        assertEquals(false, vm.getIsLoadingError().value)
        assertEquals(listOf(city), vm.getCities().value)
    }

    @Test
    fun onCityClick() {
        `when`(searchCityUseCase.execute(any(SearchCityUseCase.Params::class.java)))
            .thenReturn(Single.just(listOf(city)))
        `when`(loadHourlyWeatherUseCase.execute(LoadHourlyWeatherUseCase.Params(city.id)))
            .thenReturn(Single.just(hourlyWeather))
        `when`(saveCityUseCase.execute(SaveCityUseCase.Params(city)))
            .thenReturn(Completable.complete())

        vm.getCities().observeForever {}

        vm.searchText.value = "Wars"
        vm.afterSearchTextChanged()
        scheduler.advanceTimeBy(500, TimeUnit.MILLISECONDS)
        vm.onCityClick(city)
        scheduler.triggerActions()
        verify(loadHourlyWeatherUseCase).execute(LoadHourlyWeatherUseCase.Params(city.id))
        verify(saveCityUseCase).execute(SaveCityUseCase.Params(city))
        assertEquals(Pair(city, hourlyWeather), vm.getOnCityClick().value)
        assertEquals("", vm.searchText.value)
        assertEquals(false, vm.getIsLoadingError().value)

    }

    private fun <T> any(type: Class<T>): T {
        Mockito.any(type)
        return null as T
    }
}