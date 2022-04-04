package com.maho_ya.ui.home

import androidx.lifecycle.*
import com.maho_ya.domain.device.DeviceUseCase
import com.maho_ya.domain.review.ShouldLaunchReviewUseCase
import com.maho_ya.result.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVieModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val shouldLaunchReviewUseCase: ShouldLaunchReviewUseCase
) : ViewModel() {

    private val _device = MutableLiveData<com.maho_ya.model.Device>()
    val device: LiveData<com.maho_ya.model.Device>
        get() = _device

    fun getDevice(deviceUseCase: DeviceUseCase) {
        viewModelScope.launch {
            _device.value = deviceUseCase().data
        }
    }

    fun shouldLaunchReview() =
        shouldLaunchReviewUseCase.shouldLaunchReview()

    fun notifyReviewLaunchAttempted() {
        shouldLaunchReviewUseCase.notifyReviewLaunchAttempted()
    }
}
