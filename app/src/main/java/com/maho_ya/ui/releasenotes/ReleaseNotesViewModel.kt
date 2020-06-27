package com.maho_ya.ui.releasenotes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.maho_ya.domain.releasenotes.ReleaseNotesUseCase
import com.maho_ya.model.ReleaseNote
import com.maho_ya.result.Result
import com.maho_ya.result.data
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ReleaseNotesViewModel(
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

class ReleaseNotesViewModelFactory(
    private val releaseNotesUseCase: ReleaseNotesUseCase
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ReleaseNotesViewModel(releaseNotesUseCase) as T
    }
}

interface ReleaseNotesEventListener {

    fun onReloadClicked()
}
