package com.maho_ya.tell_me_your_dpi.ui.releasenotes

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maho_ya.tell_me_your_dpi.R
import com.maho_ya.tell_me_your_dpi.domain.releasenotes.ReleaseNotesUseCase
import com.maho_ya.tell_me_your_dpi.model.ReleaseNote
import com.maho_ya.tell_me_your_dpi.result.Result
import com.maho_ya.tell_me_your_dpi.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReleaseNoteUiState(
    val items: List<ReleaseNote> = emptyList(),
    val isLoading: Boolean = false,
    @StringRes val userMessageId: Int? = null
)

@HiltViewModel
class ReleaseNotesViewModel @Inject constructor(
    private val releaseNotesUseCase: ReleaseNotesUseCase
) : ViewModel(), ReleaseNotesEventListener {

    private val _releaseNotesUiState = MutableStateFlow(ReleaseNoteUiState(isLoading = true))

    // StateFlowに変換。startedはViewで使用する場合は WhileSubscribed を使用する
    val uiState = _releaseNotesUiState.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = _releaseNotesUiState.value
    )

    init {
        loadReleaseNotes()
    }

    override fun onReloadClicked() {
        loadReleaseNotes()
    }

    private fun loadReleaseNotes() {
        _releaseNotesUiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            // collect method of coroutine executes a suspend fun. It then collects a flow.
            releaseNotesUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> _releaseNotesUiState.update { it.copy(isLoading = true) }
                    is Result.Success -> {
                        _releaseNotesUiState.update {
                            it.copy(
                                isLoading = false,
                                items = result.data,
                                userMessageId = null
                            )
                        }
                    }
                    is Result.Error -> {
                        _releaseNotesUiState.update {
                            it.copy(
                                isLoading = false,
                                items = emptyList(),
                                userMessageId = R.string.network_error_title
                            )
                        }
                    }
                }
            }
        }
    }
}

interface ReleaseNotesEventListener {
    fun onReloadClicked()
}
