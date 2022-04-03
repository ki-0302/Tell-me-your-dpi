package com.maho_ya.ui.releasenotes

import androidx.lifecycle.*
import com.maho_ya.domain.releasenotes.ReleaseNotesUseCase
import com.maho_ya.model.ReleaseNote
import com.maho_ya.result.Result
import com.maho_ya.result.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReleaseNotesViewModel @Inject constructor(
    private val releaseNotesUseCase: ReleaseNotesUseCase
) : ViewModel(), ReleaseNotesEventListener {

    private val _releaseNotesResult = MutableLiveData<Result<List<ReleaseNote>>>()

    val releaseNotes: LiveData<List<ReleaseNote>?> = _releaseNotesResult.switchMap {
        liveData {
            emit(it.data)
        }
    }

    // map method of LiveData for main thread.
    val isLoading: LiveData<Boolean> = _releaseNotesResult.map {
        it == Result.Loading
    }

    // switchMap method of LiveData for asynchronous.
    val hasError: LiveData<Boolean> = _releaseNotesResult.switchMap {
        liveData {
            emit(it is Result.Error)
        }
    }

    init {
        loadReleaseNotes()
    }

    override fun onReloadClicked() {
        loadReleaseNotes()
    }

    private fun loadReleaseNotes() {

        viewModelScope.launch {
            // collect method of coroutine executes a suspend fun. It then collects a flow.
            releaseNotesUseCase().collect {
                _releaseNotesResult.value = it
            }
        }
    }
}

interface ReleaseNotesEventListener {

    fun onReloadClicked()
}
