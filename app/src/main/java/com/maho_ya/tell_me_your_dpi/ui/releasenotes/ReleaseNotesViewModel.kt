package com.maho_ya.tell_me_your_dpi.ui.releasenotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.maho_ya.tell_me_your_dpi.domain.releasenotes.ReleaseNotesUseCase
import com.maho_ya.tell_me_your_dpi.model.ReleaseNote
import com.maho_ya.tell_me_your_dpi.result.Result
import com.maho_ya.tell_me_your_dpi.result.data
import com.maho_ya.tell_me_your_dpi.util.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.switchMap
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Async
import javax.inject.Inject

data class ReleaseNoteUiState(
    val items: List<ReleaseNote> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: Int? = null
)


@HiltViewModel
class ReleaseNotesViewModel @Inject constructor(
    private val releaseNotesUseCase: ReleaseNotesUseCase
) : ViewModel(), ReleaseNotesEventListener {

    private val _isLoading = MutableStateFlow(false)
    private val _releaseNotesState = MutableStateFlow<Result<List<ReleaseNote>>>(Result.Loading)

    val uiState: StateFlow<ReleaseNoteUiState> = combine(
        _isLoading, _releaseNotesState
    ) { isLoading, releaseNotesState ->
        when (releaseNotesState) {
            is Result.Loading -> ReleaseNoteUiState(isLoading = true)
            is Result.Success -> {
                ReleaseNoteUiState(
                    items = releaseNotesState.data,
                    isLoading = isLoading,
                    userMessage = null
                )
            }
            is Result.Error -> {
                // TODO エラーメッセージを渡す
                ReleaseNoteUiState(isLoading = true)
            }
        }
        // FlowをStateFlowに変換。startedはViewで使用する場合は WhileSubscribed を使用する
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = ReleaseNoteUiState(isLoading = true)
    )

    init {
        loadReleaseNotes()
    }

    override fun onReloadClicked() {
        loadReleaseNotes()
    }

    private fun loadReleaseNotes() {
        _isLoading.value = true
        viewModelScope.launch {
            // collect method of coroutine executes a suspend fun. It then collects a flow.
            releaseNotesUseCase().collect {
                _releaseNotesState.value = it
                _isLoading.value = false
            }
        }
    }
}

interface ReleaseNotesEventListener {
    fun onReloadClicked()
}
