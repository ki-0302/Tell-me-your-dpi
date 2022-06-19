package com.maho_ya.tell_me_your_dpi.di

import com.maho_ya.tell_me_your_dpi.data.device.DataDeviceDataSource
import com.maho_ya.tell_me_your_dpi.data.device.DataDeviceRepository
import com.maho_ya.tell_me_your_dpi.data.device.DeviceDataSource
import com.maho_ya.tell_me_your_dpi.data.device.DeviceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Suppress("unused")
@Module
@InstallIn(ActivityComponent::class)
internal abstract class DeviceModule {

    // @Binds は所有しているコードをInjectしたい場合に使用する。@Provides でも可能だが記述が少なく済む。実装なしで済むためabstract
    @ActivityScoped
    @Binds
    abstract fun provideDeviceRepository(
        deviceRepository: DataDeviceRepository
    ): DeviceRepository

    @ActivityScoped
    @Binds
    abstract fun provideDeviceDataSource(
        deviceDataSource: DataDeviceDataSource
    ): DeviceDataSource
}
