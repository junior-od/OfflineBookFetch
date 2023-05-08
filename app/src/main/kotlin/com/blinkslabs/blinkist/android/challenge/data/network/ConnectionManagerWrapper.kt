package com.blinkslabs.blinkist.android.challenge.data.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.NetworkRequest

open class ConnectionManagerWrapper(private val connectivityManager: ConnectivityManager) {

    fun getActiveNetworkInfo(): NetworkInfo? {
        return connectivityManager.activeNetworkInfo
    }

    fun isNetworkAvailable(): Boolean {
        val networkInfo = getActiveNetworkInfo()
        return networkInfo != null && networkInfo.isConnected
    }

    fun getActiveNetwork(): Network? {
        return connectivityManager.activeNetwork
    }

    fun getNetworkCapabilities(network: Network?): NetworkCapabilities? {
        return connectivityManager.getNetworkCapabilities(network)
    }

    fun registerDefaultNetworkCallback(callback: ConnectivityManager.NetworkCallback) {
        return connectivityManager.requestNetwork(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
                .build(),
            callback
        )
    }

    fun unregisterNetworkCallback(callback: ConnectivityManager.NetworkCallback) {
        return connectivityManager.unregisterNetworkCallback(callback)
    }

    // Add other methods here as needed
}
