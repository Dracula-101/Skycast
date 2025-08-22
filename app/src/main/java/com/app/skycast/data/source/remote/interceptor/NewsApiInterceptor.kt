package com.app.skycast.data.source.remote.interceptor


import com.app.skycast.BuildConfig
import com.app.skycast.domain.model.city.UserLocation
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Singleton

@Singleton
class NewsApiInterceptor : Interceptor {

    var userLocation: UserLocation? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url
        val updatedUrl = originalUrl.newBuilder()
            .addQueryParameter("q", "${userLocation?.country} weather")
            .addQueryParameter("apiKey", BuildConfig.NEWS_API_KEY)
            .build()
        val updatedRequest = originalRequest.newBuilder()
            .url(updatedUrl)
            .build()
        return chain.proceed(updatedRequest)
    }
}