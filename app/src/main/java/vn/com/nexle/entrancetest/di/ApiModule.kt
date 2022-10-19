package vn.com.nexle.entrancetest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.com.nexle.entrancetest.data.remote.service.EntranceService
import vn.com.nexle.entrancetest.di.InjectionConstants.API_RESTFUL_BASE_URL
import vn.com.nexle.entrancetest.di.InjectionConstants.KEY_API_INTERCEPTOR
import vn.com.nexle.entrancetest.di.InjectionConstants.KEY_API_OKHTTP
import vn.com.nexle.entrancetest.di.InjectionConstants.KEY_API_RESTFUL
import vn.com.nexle.entrancetest.di.InjectionConstants.KEY_API_RESTFUL_RETROFIT
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    //-----------------------------------------Api Restful-----------------------------------------------------
    @Provides
    @Singleton
    @Named(KEY_API_INTERCEPTOR)
    fun providesApiTrackingHttpLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @Named(KEY_API_OKHTTP)
    fun provideApiTrackingOkHttpClient(@Named(KEY_API_INTERCEPTOR) interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Provides
    @Singleton
    @Named(KEY_API_RESTFUL_RETROFIT)
    fun provideApiRestfulRetrofit(
        @Named(KEY_API_OKHTTP) client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder().baseUrl(API_RESTFUL_BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGERestfulService(@Named(KEY_API_RESTFUL_RETROFIT) retrofit: Retrofit): EntranceService {
        return retrofit.create(EntranceService::class.java)
    }
}