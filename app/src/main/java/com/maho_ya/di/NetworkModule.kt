package com.maho_ya.di

import com.maho_ya.api.WebService
import com.maho_ya.data.releasenotes.DataReleaseNotesDataSource
import com.maho_ya.data.releasenotes.DataReleaseNotesRepository
import com.maho_ya.data.releasenotes.ReleaseNotesDataSource
import com.maho_ya.data.releasenotes.ReleaseNotesRepository
import com.maho_ya.tell_me_your_dpi.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
internal class NetworkModule {

    @ActivityScope
    @Provides
    fun provideWebService(): WebService {

        val okHttpClient = OkHttpClient.Builder().apply {
            readTimeout(3, TimeUnit.SECONDS)
            connectTimeout(3, TimeUnit.SECONDS)
            retryOnConnectionFailure(false)

            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
                addInterceptor(loggingInterceptor)
            }
        }.build()

        return Retrofit.Builder()
            .baseUrl("https://maho-ya.firebaseapp.com/")
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                )
            )
            .client(okHttpClient)
            .build()
            .create(WebService::class.java)
    }
}