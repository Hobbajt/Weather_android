package com.hobbajt.repository.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hobbajt.core.BuildConfig
import com.hobbajt.repository.converters.ZonedDateTimeConverter
import com.hobbajt.repository.remote.Api
import com.hobbajt.repository.remote.WeatherApiInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import org.threeten.bp.ZonedDateTime
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {

    @Singleton
    @Provides
    fun gson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(
                ZonedDateTime::class.java,
                ZonedDateTimeConverter
            )
            .serializeNulls()
            .create()
    }

    @Singleton
    @Provides
    fun httpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(WeatherApiInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun api(gson: Gson, httpClient: OkHttpClient): Api {
        val retrofit = Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BuildConfig.WEATHER_API_HOST_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(Api::class.java)
    }


}