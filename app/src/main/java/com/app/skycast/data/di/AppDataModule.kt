package com.app.skycast.data.di

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.app.skycast.BuildConfig
import com.app.skycast.data.source.local.dao.UserLocationDao
import com.app.skycast.data.source.local.db.AppDatabase
import com.app.skycast.data.source.remote.adapter.ResultCallAdapterFactory
import com.app.skycast.data.source.remote.api.CitySearchRemoteApi
import com.app.skycast.data.source.remote.api.NewsRemoteApi
import com.app.skycast.data.source.remote.api.WeatherRemoteApi
import com.app.skycast.data.source.remote.api.WeatherRemoteVcApi
import com.app.skycast.data.source.remote.interceptor.NewsApiInterceptor
import com.app.skycast.data.source.remote.interceptor.WeatherApiInterceptor
import com.app.skycast.data.source.remote.interceptor.WeatherVcApiInterceptor
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.LongSerializationPolicy
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDataModule {

    @SuppressLint("LogNotTimber")
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor { message ->
            message.chunked(5000).forEach {
                Log.v("NetworkClient", it)
            }
        }.apply {
            redactHeader(name = "Authorization")
            setLevel(
                if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                },
            )
        }
    }

    @Provides
    @Singleton
    fun provideWeatherApiInterceptor(): WeatherApiInterceptor = WeatherApiInterceptor()

    @Provides
    @Singleton
    fun provideWeatherVcApiInterceptor(): WeatherVcApiInterceptor = WeatherVcApiInterceptor()

    @Provides
    @Singleton
    fun provideNewsApiInterceptor(): NewsApiInterceptor = NewsApiInterceptor()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setLongSerializationPolicy(LongSerializationPolicy.STRING)
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .serializeNulls()
        .create()

    @Provides
    @Singleton
    fun provideWeatherRemoteApi(
        loggingInterceptor: Interceptor,
        apiTokenInterceptor: WeatherApiInterceptor,
        gson: Gson
    ): WeatherRemoteApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(apiTokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(okHttpClient)

        return retrofitBuilder
            .baseUrl(BuildConfig.WEATHER_API_BASE_URL)
            .build()
            .create(WeatherRemoteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVCWeatherApi(
        loggingInterceptor: Interceptor,
        apiTokenInterceptor: WeatherVcApiInterceptor,
        gson: Gson
    ): WeatherRemoteVcApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(apiTokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(okHttpClient)

        return retrofitBuilder
            .baseUrl(BuildConfig.WEATHER_VC_API_BASE_URL)
            .build()
            .create(WeatherRemoteVcApi::class.java)
    }

    @Provides
    @Singleton
    fun providesCitySearchApi(
        loggingInterceptor: Interceptor,
        gson: Gson
    ): CitySearchRemoteApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(okHttpClient)

        return retrofitBuilder
            .baseUrl(BuildConfig.CITY_SEARCH_API_BASE_URL)
            .build()
            .create(CitySearchRemoteApi::class.java)
    }

    @Provides
    @Singleton
    fun providesNewsRemoteApi(
        loggingInterceptor: Interceptor,
        apiTokenInterceptor: NewsApiInterceptor,
        gson: Gson
    ): NewsRemoteApi {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(apiTokenInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(okHttpClient)

        return retrofitBuilder
            .baseUrl(BuildConfig.NEWS_BASE_URL)
            .build()
            .create(NewsRemoteApi::class.java)
    }

    @Provides
    @Singleton
    fun providesAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                "app_database"
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserLocationDao(appDatabase: AppDatabase): UserLocationDao {
        return appDatabase.userLocationDao()
    }
}