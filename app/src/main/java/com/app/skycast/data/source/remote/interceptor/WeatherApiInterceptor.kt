package com.app.skycast.data.source.remote.interceptor

import com.app.skycast.BuildConfig
import com.app.skycast.domain.model.city.UserLocation
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Singleton

@Singleton
class WeatherApiInterceptor : Interceptor {

    var userLocation: UserLocation? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val updatedUrl = originalUrl.newBuilder()
            .addQueryParameter("key", BuildConfig.WEATHER_API_KEY)
            .addQueryParameter("q", userLocation?.city ?: throw IllegalStateException("User location not set"))
            .build()
        val updatedRequest = originalRequest.newBuilder()
            .url(updatedUrl)
            .build()
        return chain.proceed(updatedRequest)
    }
}