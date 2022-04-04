package com.maho_ya.tell_me_your_dpi.domain.releasenotes

import com.maho_ya.tell_me_your_dpi.data.releasenotes.ReleaseNotesRepository
import com.maho_ya.tell_me_your_dpi.domain.FlowUseCase
import com.maho_ya.tell_me_your_dpi.model.ReleaseNote
import com.maho_ya.tell_me_your_dpi.result.Result
import com.maho_ya.tell_me_your_dpi.result.Result.Error
import com.maho_ya.tell_me_your_dpi.result.Result.Loading
import com.maho_ya.tell_me_your_dpi.result.Result.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReleaseNotesUseCase @Inject constructor(
    private val releaseNotesRepository: ReleaseNotesRepository
) : FlowUseCase<List<ReleaseNote>>() {

    override suspend fun execute(): Flow<Result<List<ReleaseNote>>> {

        return releaseNotesRepository.getReleaseNotes().map { result ->
            when (result) {
                is Success -> {
                    Success(
                        result.data.releaseNotes.sortedWith(
                            compareByDescending<ReleaseNote> { it.date }
                                .thenByDescending { it.appVersion }
                        )
                    )
                }
                is Error -> result
                is Loading -> result
            }
        }
    }
}
