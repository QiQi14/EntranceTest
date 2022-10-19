package vn.com.nexle.entrancetest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.com.nexle.entrancetest.data.local.source.PreferenceSource
import vn.com.nexle.entrancetest.data.remote.source.EntranceRemoteDataSource
import vn.com.nexle.entrancetest.data.repository.EntranceRepository
import vn.com.nexle.entrancetest.data.repository.EntranceRepositoryImp
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideGERepository(
        remoteDataSource: EntranceRemoteDataSource,
        localDataSource: PreferenceSource
    ): EntranceRepository {
        return EntranceRepositoryImp(remoteDataSource, localDataSource)
    }
}