package com.maho_ya.tell_me_your_dpi.ui.home

import androidx.lifecycle.*
import com.maho_ya.tell_me_your_dpi.domain.device.DeviceUseCase
import com.maho_ya.tell_me_your_dpi.domain.review.ShouldLaunchReviewUseCase
import com.maho_ya.tell_me_your_dpi.result.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVieModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val shouldLaunchReviewUseCase: ShouldLaunchReviewUseCase
) : ViewModel() {

    private val _device = MutableLiveData<com.maho_ya.tell_me_your_dpi.model.Device>()
    val device: LiveData<com.maho_ya.tell_me_your_dpi.model.Device>
        get() = _device

    fun getDevice(deviceUseCase: DeviceUseCase) {
        viewModelScope.launch {
            deviceUseCase().data?.let {
                _device.value = it
            }
        }
    }

    fun shouldLaunchReview() =
        shouldLaunchReviewUseCase.shouldLaunchReview()

    fun notifyReviewLaunchAttempted() {
        shouldLaunchReviewUseCase.notifyReviewLaunchAttempted()
    }
}
