package com.app.skycast.data.source.remote.interceptor


import com.app.skycast.BuildConfig
import com.app.skycast.domain.model.city.UserLocation
import com.app.skycast.domain.model.units.UnitType
import okhttp3.Interceptor
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Singleton

@Singleton
class WeatherVcApiInterceptor : Interceptor {

    var userLocation: UserLocation? = null
    var userMeasurement: UnitType? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentDate = Date()
        val forecastDate = Date(currentDate.time + 7 * 24 * 60 * 60 * 1000)
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val updatedUrl = originalUrl.newBuilder()
            .addPathSegment("${userLocation?.city}, ${userLocation?.country}")
            .addPathSegment(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate))
            .addPathSegment(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(forecastDate))
            .addQueryParameter("unitGroup", userMeasurement?.toString() ?: throw IllegalStateException("User measurement not set"))
            .addQueryParameter("key", BuildConfig.WEATHER_VC_API_KEY)
            .addQueryParameter("contentType", "json")
            .build()
        val updatedRequest = originalRequest.newBuilder()
            .url(updatedUrl)
            .build()
        return chain.proceed(updatedRequest)
    }


}