package com.maho_ya.data.releasenotes

import com.maho_ya.api.WebService
import javax.inject.Inject

interface ReleaseNotesDataSource {

    suspend fun getReleaseNotesResults(): com.maho_ya.model.ReleaseNotesResults
}

class DataReleaseNotesDataSource @Inject constructor(
    private val webService: WebService
) : ReleaseNotesDataSource {

    override suspend fun getReleaseNotesResults(): com.maho_ya.model.ReleaseNotesResults =
        webService.releaseNotesResults()
}
