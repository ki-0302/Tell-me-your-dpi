package com.maho_ya.tell_me_your_dpi.data.releasenotes

import android.annotation.SuppressLint
import com.maho_ya.tell_me_your_dpi.model.ReleaseNotesResults
import com.maho_ya.tell_me_your_dpi.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import javax.inject.Inject

interface ReleaseNotesRepository {

    suspend fun getReleaseNotes(): Flow<Result<ReleaseNotesResults>>
}

class DataReleaseNotesRepository @Inject constructor(
    private val releaseNotesDataSource: ReleaseNotesDataSource
) : ReleaseNotesRepository {

    override suspend fun getReleaseNotes(): Flow<Result<ReleaseNotesResults>> {

        return flow {
            emit(Result.Loading)

            try {
                val releaseNotesResults = releaseNotesDataSource.getReleaseNotesResults()

                // Format date.
                releaseNotesResults.releaseNotes.forEach {
                    it.date = formatDate(it.date)
                }

                emit(
                    Result.Success(
                        releaseNotesResults
                    )
                )
            } catch (e: Exception) {
                Timber.d(e)
                emit(Result.Error(e))
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun formatDate(date: String?): String {

        date ?: return ""
        return try {
            val d = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'")
                .parse(date)
            SimpleDateFormat("yyyy/MM/dd HH:mm")
                .format(d!!)
        } catch (e: ParseException) {
            ""
        }
    }
}
