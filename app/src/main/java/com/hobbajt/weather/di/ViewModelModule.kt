package com.hobbajt.weather.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hobbajt.cityweather.citylist.CityListVM
import com.hobbajt.cityweather.weather.WeatherVM
import com.hobbajt.presentation.base.ViewModelFactory
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun viewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CityListVM::class)
    abstract fun cityListVM(vm: CityListVM): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WeatherVM::class)
    abstract fun weatherVM(vm: WeatherVM): ViewModel
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

