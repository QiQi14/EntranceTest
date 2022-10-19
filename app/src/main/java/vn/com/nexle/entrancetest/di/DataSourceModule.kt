package vn.com.nexle.entrancetest.di

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.com.nexle.entrancetest.data.local.service.PreferencesManager
import vn.com.nexle.entrancetest.data.local.source.PreferenceSource
import vn.com.nexle.entrancetest.data.local.source.PreferenceSourceImp
import vn.com.nexle.entrancetest.data.remote.service.EntranceService
import vn.com.nexle.entrancetest.data.remote.source.EntranceRemoteDataSource
import vn.com.nexle.entrancetest.data.remote.source.EntranceRemoteDataSourceImp
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    //------------------------------------Local data source---------------------------------
    @Provides
    @Singleton
    fun provideSharedPreferencesManager(
        application: Application,
    ): PreferencesManager {
        return PreferencesManager(application.applicationContext)
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(
        sharedPreferences: PreferencesManager
    ): PreferenceSource {
        return PreferenceSourceImp(sharedPreferences)
    }

    //------------------------------------Remote data source--------------------------------
    @Provides
    @Singleton
    fun provideRemoteDataSource(
        restfulService: EntranceService,
    ): EntranceRemoteDataSource {
        return EntranceRemoteDataSourceImp(restfulService)
    }
}