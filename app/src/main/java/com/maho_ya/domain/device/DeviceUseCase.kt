package com.maho_ya.domain.device

import com.maho_ya.data.device.DeviceRepository
import com.maho_ya.domain.UseCase
import com.maho_ya.model.Device
import javax.inject.Inject

class DeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) : UseCase<com.maho_ya.model.Device>() {

    override suspend fun execute(): com.maho_ya.model.Device = deviceRepository
        .getDevice()
}
