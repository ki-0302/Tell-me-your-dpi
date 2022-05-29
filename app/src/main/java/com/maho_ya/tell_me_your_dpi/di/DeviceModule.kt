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

@InstallIn(ActivityComponent::class)
@Module
internal abstract class DeviceModule {

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
