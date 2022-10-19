package vn.com.galaxy.geparentalcontrol.base.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import vn.com.nexle.entrancetest.util.ConnectivityDetector
import vn.com.nexle.entrancetest.util.NetworkStatus
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject

/**
 * Class to check whether the device is currently connected to the internet or not
 * Reference: https://medium.com/@elye.project/android-intercept-on-no-internet-connection-acb91d305357
 */
class ConnectivityDetectorImp @Inject constructor(
    @ApplicationContext val context: Context
) : ConnectivityDetector {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun isConnected(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            postAndroidMInternetCheck(connectivityManager)
        } else {
            preAndroidMInternetCheck(connectivityManager)
        }
    }

    override fun networkStatus() = callbackFlow {
        val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
            var available = false
            override fun onUnavailable() {
                available = false
                trySend(NetworkStatus.UNAVAILABLE)
            }

            override fun onAvailable(network: Network) {
                available = true
                CoroutineScope(Dispatchers.IO).launch {
                    while(available) {
                        kotlinx.coroutines.delay(1000L)
                        checkAvailability().collect {
                            if (it) {
                                trySend(NetworkStatus.AVAILABLE)
                            } else {
                                trySend(NetworkStatus.UNAVAILABLE)
                            }
                        }
                    }

                }
            }

            override fun onLost(network: Network) {
                available = false
                trySend(NetworkStatus.UNAVAILABLE)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkStatusCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkStatusCallback)
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun checkAvailability() : Flow<Boolean> = flow {
        val isConnect = try {
            Socket().use {
                it.connect(InetSocketAddress("8.8.8.8", 53))
            }
            true
        } catch (e: Exception){
            false
        }
        emit(isConnect)
    }.flowOn(Dispatchers.IO)

    @RequiresApi(Build.VERSION_CODES.M)
    private fun postAndroidMInternetCheck(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork
        val connection =
            connectivityManager.getNetworkCapabilities(network)

        return connection != null && (
                connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || connection.hasTransport(
                    NetworkCapabilities.TRANSPORT_ETHERNET
                ))
    }

    /**
     * Reference: https://medium.com/@elye.project/android-intercept-on-no-internet-connection-acb91d305357
     * */
    @Suppress("DEPRECATION")
    private fun preAndroidMInternetCheck(connectivityManager: ConnectivityManager): Boolean {
        val activeNetwork = connectivityManager.activeNetworkInfo
        if (activeNetwork != null) {
            return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                    activeNetwork.type == ConnectivityManager.TYPE_MOBILE ||
                    activeNetwork.type == ConnectivityManager.TYPE_ETHERNET)
        }
        return false
    }
}