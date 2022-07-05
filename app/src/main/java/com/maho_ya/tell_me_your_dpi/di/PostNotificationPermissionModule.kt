package com.maho_ya.tell_me_your_dpi.di

import com.maho_ya.tell_me_your_dpi.domain.notification.FirstPostNotificationPermissionUseCase
import com.maho_ya.tell_me_your_dpi.domain.notification.FirstPostNotificationPermissionUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class PostNotificationPermissionModule {

    @Singleton
    @Binds
    abstract fun provideFirstPostNotificationPermissionUseCase(
        firstPostNotificationPermissionUseCase: FirstPostNotificationPermissionUseCaseImpl
    ): FirstPostNotificationPermissionUseCase
}
