package com.maho_ya.tell_me_your_dpi.data.device

import android.content.Context
import com.maho_ya.tell_me_your_dpi.model.Device

interface DeviceRepository {
    suspend fun getDevice(): Device
}

class DataDeviceRepository(
    context: Context?,
    private val deviceDataSource: DeviceDataSource = DataDeviceDataSource(context)
) : DeviceRepository {

    override suspend fun getDevice(): Device = deviceDataSource
        .getDevice()
}
