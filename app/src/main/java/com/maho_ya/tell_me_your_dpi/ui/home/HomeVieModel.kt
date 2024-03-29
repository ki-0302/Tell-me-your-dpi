package com.maho_ya.tell_me_your_dpi.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maho_ya.tell_me_your_dpi.domain.device.DeviceUseCase
import com.maho_ya.tell_me_your_dpi.domain.notification.FirstPostNotificationPermissionUseCase
import com.maho_ya.tell_me_your_dpi.domain.review.ShouldLaunchReviewUseCase
import com.maho_ya.tell_me_your_dpi.model.Device
import com.maho_ya.tell_me_your_dpi.result.Result
import com.maho_ya.tell_me_your_dpi.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val device: Device? = null,
    val isLoading: Boolean = false,
    val isFirstPostNotificationsPermission: Boolean = false,
    val userMessage: String? = null,
    val shouldLaunchReview: Boolean = false,
    val shouldLaunchIntroDialog: Boolean = false,
    val shouldLaunchSnackBar: Boolean = false
)

@HiltViewModel
class HomeVieModel @Inject constructor(
    private val shouldLaunchReviewUseCase: ShouldLaunchReviewUseCase,
    private val firstPostNotificationPermissionUseCase: FirstPostNotificationPermissionUseCase
) : ViewModel() {

    private val _homeUiState = MutableStateFlow(
        HomeUiState(
            isLoading = true,
            isFirstPostNotificationsPermission = isFirstPostNotificationPermission()
        )
    )

    // StateFlowに変換。startedはViewで使用する場合は WhileSubscribed を使用する
    val uiState = _homeUiState.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = _homeUiState.value
    )

    fun getDevice(deviceUseCase: DeviceUseCase) {
        _homeUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            deviceUseCase().let { result ->
                when (result) {
                    is Result.Loading -> _homeUiState.update { it.copy(isLoading = true) }
                    is Result.Success -> {
                        _homeUiState.update {
                            it.copy(
                                isLoading = false,
                                device = result.data,
                                userMessage = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _homeUiState.update {
                            it.copy(
                                isLoading = false,
                                device = null,
                                userMessage = result.exception.message
                            )
                        }
                    }
                }
            }

            _homeUiState.update {
                it.copy(
                    shouldLaunchReview = shouldLaunchReview(),
                    shouldLaunchIntroDialog = false
                )
            }
        }
    }

    fun showSnackBar() =
        _homeUiState.update {
            it.copy(shouldLaunchSnackBar = true)
        }

    fun shownSnackBar() =
        _homeUiState.update {
            it.copy(shouldLaunchSnackBar = false)
        }

    private fun isFirstPostNotificationPermission() =
        firstPostNotificationPermissionUseCase.isFirstPostNotificationPermission()

    private fun shouldLaunchReview() =
        shouldLaunchReviewUseCase.shouldLaunchReview()

    fun firstPostNotificationPermissionCompleted() {
        _homeUiState.update { it.copy(isFirstPostNotificationsPermission = false) }
        firstPostNotificationPermissionUseCase.firstPostNotificationPermissionCompleted()
    }

    fun notifyReviewLaunchAttempted() {
        _homeUiState.update { it.copy(shouldLaunchReview = false) }
        shouldLaunchReviewUseCase.notifyReviewLaunchAttempted()
    }
}
