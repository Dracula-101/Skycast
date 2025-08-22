package com.app.skycast.domain.manager

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Parcelable
import com.app.skycast.domain.util.bufferedMutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.parcelize.Parcelize

class NetworkManager(
    private val connectivityService: ConnectivityManager,
) {

    private val _networkStatus = bufferedMutableSharedFlow<NetworkStatus?>(replay = 1)
    val networkStatusFlow = _networkStatus.map { it ?: NetworkStatus.UNKNOWN }

    private val _networkType = bufferedMutableSharedFlow<NetworkType?>(replay = 1)
    val networkTypeFlow = _networkType.map { it ?: NetworkType.Unknown }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _networkStatus.tryEmit(NetworkStatus.CONNECTED)
            _networkType.tryEmit(getNetworkType(network))
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _networkStatus.tryEmit(NetworkStatus.DISCONNECTED)
            _networkType.tryEmit(NetworkType.Unknown)
        }

        override fun onUnavailable() {
            super.onUnavailable()
            _networkStatus.tryEmit(NetworkStatus.DISCONNECTED)
            _networkType.tryEmit(NetworkType.Unknown)
        }

        override fun onLosing(network: Network, maxMsToLive: Int) {
            super.onLosing(network, maxMsToLive)
            _networkStatus.tryEmit(NetworkStatus.DISCONNECTING)
        }
    }

    fun registerNetworkCallback() {
        connectivityService.registerDefaultNetworkCallback(networkCallback)
    }

    private fun getNetworkType(network: Network): NetworkType {
        val networkCapabilities = connectivityService.getNetworkCapabilities(network)
        return when {
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true -> NetworkType.Wifi
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true -> NetworkType.MobileData
            networkCapabilities?.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) == true -> NetworkType.Ethernet
            else -> NetworkType.Unknown
        }
    }

    fun unregisterNetworkCallback() {
        connectivityService.unregisterNetworkCallback(networkCallback)
    }

}

@Parcelize
enum class NetworkStatus : Parcelable {
    CONNECTED,
    DISCONNECTED,
    DISCONNECTING,
    UNKNOWN
}

@Parcelize
sealed class NetworkType : Parcelable {
    data object Wifi : NetworkType()
    data object MobileData : NetworkType()
    data object Ethernet : NetworkType()
    data object Unknown : NetworkType()
}