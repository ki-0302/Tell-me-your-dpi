package com.maho_ya.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.maho_ya.domain.device.DeviceUseCase
import com.maho_ya.model.Device
import com.maho_ya.result.data
import kotlinx.coroutines.launch

class HomeVieModel @ViewModelInject constructor(
    private val deviceUseCase: DeviceUseCase
) : ViewModel() {

    private val _device = MutableLiveData<com.maho_ya.model.Device>()
    val device: LiveData<com.maho_ya.model.Device>
        get() = _device

    init {
        viewModelScope.launch {
            _device.value = deviceUseCase().data
        }
    }
}

class HomeViewModelFactory(
    private val deviceUseCase: DeviceUseCase
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeVieModel(deviceUseCase) as T
    }
}
