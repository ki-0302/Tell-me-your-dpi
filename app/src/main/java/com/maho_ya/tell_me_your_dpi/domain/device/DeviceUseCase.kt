package com.maho_ya.tell_me_your_dpi.domain.device

import com.maho_ya.tell_me_your_dpi.data.device.DeviceRepository
import com.maho_ya.tell_me_your_dpi.domain.UseCase
import com.maho_ya.tell_me_your_dpi.model.Device
import javax.inject.Inject

class DeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) : UseCase<Device>() {

    override suspend fun execute(): Device = deviceRepository
        .getDevice()
}
