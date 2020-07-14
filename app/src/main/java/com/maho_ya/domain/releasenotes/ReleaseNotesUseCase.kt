package com.maho_ya.domain.releasenotes

import com.maho_ya.data.releasenotes.ReleaseNotesRepository
import com.maho_ya.domain.FlowUseCase
import com.maho_ya.model.ReleaseNote
import com.maho_ya.result.Result
import com.maho_ya.result.Result.Error
import com.maho_ya.result.Result.Loading
import com.maho_ya.result.Result.Success
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReleaseNotesUseCase @Inject constructor(
    private val releaseNotesRepository: ReleaseNotesRepository
) : FlowUseCase<List<com.maho_ya.model.ReleaseNote>>() {

    override suspend fun execute(): Flow<Result<List<com.maho_ya.model.ReleaseNote>>> {

        return releaseNotesRepository.getReleaseNotes().map { result ->
            when (result) {
                is Success -> {
                    Success(
                        result.data.releaseNotes.sortedWith(
                            compareByDescending<com.maho_ya.model.ReleaseNote> { it.date }
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
