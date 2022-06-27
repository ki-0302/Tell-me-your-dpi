package com.maho_ya.tell_me_your_dpi.di

import android.content.Context
import com.maho_ya.tell_me_your_dpi.data.prefs.PreferenceStorage
import com.maho_ya.tell_me_your_dpi.data.prefs.SharedPreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePreferenceStorage(
        @ApplicationContext context: Context
    ): PreferenceStorage =
        SharedPreferenceStorage(context)
}
