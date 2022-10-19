package vn.com.nexle.entrancetest.di

import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.com.nexle.entrancetest.util.ConnectivityDetector
import vn.com.galaxy.geparentalcontrol.base.connectivity.ConnectivityDetectorImp

@Module
@InstallIn(SingletonComponent::class)
interface ConnectivityModule {
    @Binds
    @Reusable
    fun bindsConnectivityDetectorImpl(
        connectivityDetectorImp: ConnectivityDetectorImp
    ): ConnectivityDetector
}