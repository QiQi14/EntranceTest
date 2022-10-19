package vn.com.nexle.entrancetest.util

import kotlinx.coroutines.flow.Flow

interface ConnectivityDetector {
    fun isConnected(): Boolean
    fun networkStatus() : Flow<NetworkStatus>
}

sealed class NetworkStatus {
    object AVAILABLE : NetworkStatus()
    object UNAVAILABLE : NetworkStatus()
}