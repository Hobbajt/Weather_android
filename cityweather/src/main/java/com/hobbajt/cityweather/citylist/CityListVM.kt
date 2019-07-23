package com.hobbajt.cityweather.citylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.hobbajt.cityweather.R
import com.hobbajt.core.schedulerprovider.SchedulerProvider
import com.hobbajt.domain.model.City
import com.hobbajt.domain.model.HourlyWeather
import com.hobbajt.domain.usecases.LoadHistoryCitiesUseCase
import com.hobbajt.domain.usecases.LoadHourlyWeatherUseCase
import com.hobbajt.domain.usecases.SaveCityUseCase
import com.hobbajt.domain.usecases.SearchCityUseCase
import com.hobbajt.presentation.ActionLiveData
import com.hobbajt.presentation.base.SchedulerViewModel
import com.hobbajt.presentation.base.Toolbar
import com.hobbajt.presentation.base.ToolbarOwner
import io.reactivex.Notification
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CityListVM @Inject constructor(
    private val searchCityUseCase: SearchCityUseCase,
    private val saveCityUseCase: SaveCityUseCase,
    loadHistoryCitiesUseCase: LoadHistoryCitiesUseCase,
    private val loadHourlyWeatherUseCase: LoadHourlyWeatherUseCase,
    private val schedulerProvider: SchedulerProvider
) : SchedulerViewModel(schedulerProvider), ToolbarOwner {

    companion object {
        private const val SEARCH_WRITE_DELAY = 500L
        /*
        The regular expression "a-zA-Z" given in the task requirements doesn't work.
        The Working version of this regex is "[a-zA-Z]+", which doesn't allow searching text containing
        Polish letters (e.g. Łódź) and spaces (e.g. New York). To allow searching Polish letters and spaces
        "[a-zA-ZżźćńółęąśŻŹĆĄŚĘŁÓŃ ]+" regex should be used. To allow searching all international letters and spaces
        "^[\\p{L} ]+" regex should be used.
         */
        private const val SEARCH_VALIDATION_REGEX = "[a-zA-Z]+"
    }

    override var toolbar = MutableLiveData<Toolbar>().apply {
        value = Toolbar(
            titleId = R.string.city_list_title,
            colorId = R.color.darkPurple
        )
    }

    private val searchCities = MutableLiveData<List<City>>(emptyList())
    private val historyCities: LiveData<List<City>> = loadHistoryCitiesUseCase.execute(Unit)
    private val cities = MediatorLiveData<List<City>>().apply { value = emptyList() }

    private val isSearchTextValid = MutableLiveData(true)
    private val isLoadingError = MutableLiveData(false)
    private val isLoading = MutableLiveData(false)
    private val onCityClick = ActionLiveData<Pair<City, List<HourlyWeather>>>()
    private val onCityWeatherLoadError = ActionLiveData<Unit>()
    private val areHistoryCitiesDisplayed = MutableLiveData(true)
    private val citySearchSubject: BehaviorSubject<String> = BehaviorSubject.create()
    // Two-way data binding
    val searchText = MutableLiveData<String>()

    fun getCities(): LiveData<List<City>> = cities
    fun getIsSearchTextValid(): LiveData<Boolean> = isSearchTextValid
    fun getIsLoadingError(): LiveData<Boolean> = isLoadingError
    fun getIsLoading(): LiveData<Boolean> = isLoading
    fun getOnCityClick(): LiveData<Pair<City, List<HourlyWeather>>> = onCityClick
    fun getOnCityWeatherLoadError(): LiveData<Unit> = onCityWeatherLoadError
    fun getAreHistoryCitiesDisplayed(): LiveData<Boolean> = areHistoryCitiesDisplayed

    init {
        initCitiesSources()
        loadCitiesOnSearch()
    }

    private fun loadCitiesOnSearch() {
        schedule(
            citySearchSubject.throttleLast(SEARCH_WRITE_DELAY, TimeUnit.MILLISECONDS, schedulerProvider.computation())
                .switchMap {
                    searchCityUseCase.execute(SearchCityUseCase.Params(it))
                        .map { searchCities -> Notification.createOnNext(searchCities) }
                        .onErrorReturn { error -> Notification.createOnError(error) }
                        .toObservable()
                },
            nextFun = { result ->
                isLoading.value = false
                isLoadingError.value = result.isOnError
                searchCities.value = result.value
                if (result.isOnError) {
                    Timber.e(result.error)
                }
            })
    }

    private fun initCitiesSources() {
        cities.addSource(historyCities) { displayHistoryCities() }
        cities.addSource(searchCities) { displaySearchCities() }
    }

    private fun displaySearchCities() {
        if (!searchText.value.isNullOrBlank()) {
            cities.value = searchCities.value
            areHistoryCitiesDisplayed.value = false
        }
    }

    private fun displayHistoryCities() {
        if (searchText.value.isNullOrBlank()) {
            cities.value = historyCities.value
            areHistoryCitiesDisplayed.value = true
        }
    }

    fun onCityClick(city: City) {
        if (cities.value?.contains(city) == true) {
            loadWeather(city)
        }
    }

    private fun loadWeather(city: City) {
        schedule(loadHourlyWeatherUseCase.execute(LoadHourlyWeatherUseCase.Params(city.id))
            .flatMap {
                saveCityUseCase.execute(SaveCityUseCase.Params(city))
                    .andThen(Single.defer { Single.just(it) })
            },
            resultFun = { openWeather(city, it) },
            errorFun = {
                Timber.e(it)
                onCityWeatherLoadError.sendAction(Unit)
            }
        )
    }

    private fun openWeather(city: City, hourlyWeather: List<HourlyWeather>) {
        onCityClick.sendAction(Pair(city, hourlyWeather))
        searchText.value = ""
        afterSearchTextChanged()
    }

    fun afterSearchTextChanged() {
        isLoadingError.value = false
        searchText.value?.let {
            if (it.isBlank()) {
                isSearchTextValid.value = true
                displayHistoryCities()
            } else if (!isSearchTextValid(it)) {
                isSearchTextValid.value = false
            } else {
                isLoading.value = true
                isSearchTextValid.value = true
                citySearchSubject.onNext(it)
            }
        }
    }

    private fun isSearchTextValid(searchText: String): Boolean {
        return searchText.matches(SEARCH_VALIDATION_REGEX.toRegex())
    }
}