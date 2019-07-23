package com.hobbajt.repository.remote

import com.hobbajt.core.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class WeatherApiInterceptor : Interceptor {
    companion object {
        private const val API_KEY_TAG = "apikey"
        private const val LANGUAGE_TAG = "language"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalHttpUrl = original.url()

        val url = originalHttpUrl.newBuilder()
            .addQueryParameter(API_KEY_TAG, BuildConfig.WEATHER_API_KEY)
            .addQueryParameter(LANGUAGE_TAG, createLanguageCode())
            .build()

        val request = original.newBuilder()
            .url(url).build()
        return chain.proceed(request)
    }

    private fun createLanguageCode(): String {
        return Locale.getDefault().toString().toLowerCase().replace('_', '-')
    }


}