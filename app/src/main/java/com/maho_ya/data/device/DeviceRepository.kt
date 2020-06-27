package com.maho_ya.data.device

import com.maho_ya.model.Device

interface DeviceRepository {
    suspend fun getDevice(): Device
}

class DataDeviceRepository(
    private val deviceDataSource: DeviceDataSource
) : DeviceRepository {

    override suspend fun getDevice(): Device = deviceDataSource
        .getDevice()
}
