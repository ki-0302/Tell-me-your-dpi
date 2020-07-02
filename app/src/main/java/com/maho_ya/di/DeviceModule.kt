package com.maho_ya.di

import com.maho_ya.data.device.DataDeviceDataSource
import com.maho_ya.data.device.DataDeviceRepository
import com.maho_ya.data.device.DeviceDataSource
import com.maho_ya.data.device.DeviceRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
internal abstract class DeviceModule {

    @Singleton
    @Binds
    abstract fun provideDeviceRepository(
        deviceRepository: DataDeviceRepository
    ): DeviceRepository

    @Singleton
    @Binds
    abstract fun provideDeviceDataSource(
        deviceDataSource: DataDeviceDataSource
    ): DeviceDataSource
}
