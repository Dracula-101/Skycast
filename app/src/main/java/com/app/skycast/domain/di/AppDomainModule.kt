package com.app.skycast.domain.di

import android.content.Context
import android.location.Geocoder
import android.location.LocationManager
import android.net.ConnectivityManager
import android.telephony.TelephonyManager
import com.app.skycast.data.source.local.db.AppDatabase
import com.app.skycast.data.source.remote.api.CitySearchRemoteApi
import com.app.skycast.data.source.remote.api.NewsRemoteApi
import com.app.skycast.data.source.remote.api.WeatherRemoteApi
import com.app.skycast.data.source.remote.api.WeatherRemoteVcApi
import com.app.skycast.domain.manager.AppConfigManager
import com.app.skycast.domain.manager.AppLocationManager
import com.app.skycast.domain.manager.AppNavigationManager
import com.app.skycast.domain.manager.NetworkManager
import com.app.skycast.domain.manager.UserConfigManager
import com.app.skycast.domain.manager.WeatherInfoManager
import com.app.skycast.domain.repository.city_search.CitySearchRepoImpl
import com.app.skycast.domain.repository.city_search.CitySearchRepository
import com.app.skycast.domain.repository.config.AppConfigRepoImpl
import com.app.skycast.domain.repository.config.AppConfigRepository
import com.app.skycast.domain.repository.navigation.AppNavigationRepoImpl
import com.app.skycast.domain.repository.navigation.AppNavigationRepository
import com.app.skycast.domain.repository.news.NewsRepository
import com.app.skycast.domain.repository.news.NewsRepositoryImpl
import com.app.skycast.domain.repository.user.config.UserConfigRepoImpl
import com.app.skycast.domain.repository.user.config.UserConfigRepository
import com.app.skycast.domain.repository.weather.WeatherRepoImpl
import com.app.skycast.domain.repository.weather.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.Locale
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDomainModule {

    @Provides
    @Singleton
    fun provideLocationManager(
        @ApplicationContext context: Context,
        userConfigManager: UserConfigManager,
    ): AppLocationManager {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val localeList = context.resources.configuration.locales
        val geocoder = Geocoder(context, Locale.getDefault())
        return AppLocationManager(
            locationManager = locationManager,
            telephonyManager = telephonyManager,
            localeList = localeList,
            geoCoder = geocoder,
            userConfigManager = userConfigManager
        )
    }

    @Provides
    @Singleton
    fun provideAppNavigationManager(@ApplicationContext context: Context): AppNavigationManager {
        val sharedPrefs = context.getSharedPreferences("app_navigation", Context.MODE_PRIVATE)
        return AppNavigationManager(sharedPrefs)
    }

    @Provides
    @Singleton
    fun provideAppNavigationRepository(appNavigationManager: AppNavigationManager): AppNavigationRepository {
        return AppNavigationRepoImpl(appNavigationManager)
    }

    @Provides
    @Singleton
    fun provideCitySearchRepository(
        citySearchApi: CitySearchRemoteApi
    ): CitySearchRepository {
        return CitySearchRepoImpl(citySearchApi = citySearchApi)
    }

    @Provides
    @Singleton
    fun provideUserConfigManager(@ApplicationContext context: Context): UserConfigManager {
        val sharedPrefs = context.getSharedPreferences("user_config", Context.MODE_PRIVATE)
        return UserConfigManager(sharedPrefs)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userConfigManager: UserConfigManager,
        appDatabase: AppDatabase
    ): UserConfigRepository {
        return UserConfigRepoImpl(
            userConfigManager = userConfigManager,
            userLocationDao = appDatabase.userLocationDao()
        )
    }

    @Provides
    @Singleton
    fun provideWeatherInfoManager(@ApplicationContext context: Context): WeatherInfoManager {
        val sharedPrefs = context.getSharedPreferences("weather_info", Context.MODE_PRIVATE)
        return WeatherInfoManager(sharedPrefs)
    }

    @Provides
    @Singleton
    fun provideWeatherInfoRepository(
        weatherApi: WeatherRemoteApi,
        weatherInfoManager: WeatherInfoManager,
        weatherVcApi: WeatherRemoteVcApi,
        userConfigManager: UserConfigManager,
    ): WeatherRepository {
        return WeatherRepoImpl(
            weatherInfoManager = weatherInfoManager,
            weatherApi = weatherApi,
            weatherVcApi = weatherVcApi,
            userConfigManager = userConfigManager,
        )
    }

    @Provides
    @Singleton
    fun provideNetworkManager(@ApplicationContext context: Context): NetworkManager {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return NetworkManager(connectivityManager)
    }

    @Provides
    @Singleton
    fun provideAppConfigManager(@ApplicationContext context: Context): AppConfigManager {
        val sharedPrefs = context.getSharedPreferences("app_config", Context.MODE_PRIVATE)
        return AppConfigManager(sharedPrefs)
    }

    @Provides
    @Singleton
    fun provideAppConfigRepository(
        appConfigManager: AppConfigManager,
    ): AppConfigRepository {
        return AppConfigRepoImpl(appConfigManager)
    }

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsApi: NewsRemoteApi
    ): NewsRepository {
        return NewsRepositoryImpl(newsApi = newsApi)
    }

}