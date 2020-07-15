package com.maho_ya.data.device

import com.maho_ya.model.Device
import javax.inject.Inject

interface DeviceRepository {
    suspend fun getDevice(): Device
}

class DataDeviceRepository @Inject constructor(
    private val deviceDataSource: DeviceDataSource
) : DeviceRepository {

    override suspend fun getDevice(): Device = deviceDataSource
        .getDevice()
}
