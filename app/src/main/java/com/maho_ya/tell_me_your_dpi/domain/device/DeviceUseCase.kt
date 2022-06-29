package com.maho_ya.tell_me_your_dpi.domain.device

import android.content.Context
import com.maho_ya.tell_me_your_dpi.data.device.DataDeviceRepository
import com.maho_ya.tell_me_your_dpi.data.device.DeviceRepository
import com.maho_ya.tell_me_your_dpi.domain.UseCase
import com.maho_ya.tell_me_your_dpi.model.Device

class DeviceUseCase constructor(
    context: Context?,
    private val deviceRepository: DeviceRepository = DataDeviceRepository(context)
) : UseCase<Device>() {

    override suspend fun execute(): Device = deviceRepository.getDevice()
}
