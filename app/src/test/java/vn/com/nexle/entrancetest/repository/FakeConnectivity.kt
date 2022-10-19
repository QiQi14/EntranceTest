package vn.com.nexle.entrancetest.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vn.com.nexle.entrancetest.util.ConnectivityDetector
import vn.com.nexle.entrancetest.util.NetworkStatus

class FakeConnectivity : ConnectivityDetector {
    override fun isConnected(): Boolean {
        return true
    }

    override fun networkStatus(): Flow<NetworkStatus> = flow {
        emit(NetworkStatus.AVAILABLE)
    }
}