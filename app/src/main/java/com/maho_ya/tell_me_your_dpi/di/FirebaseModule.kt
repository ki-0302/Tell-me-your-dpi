package com.maho_ya.tell_me_your_dpi.di

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal object FirebaseModule {

    // 所有していないコードをInjectする場合に使用する
    @Provides
    fun provideAnalytics(): FirebaseAnalytics = Firebase.analytics
}
