package com.maho_ya.data.releasenotes

import com.maho_ya.api.WebService
import com.maho_ya.model.ReleaseNotesResults
import javax.inject.Inject

interface ReleaseNotesDataSource {

    suspend fun getReleaseNotesResults(): ReleaseNotesResults
}

class DataReleaseNotesDataSource @Inject constructor(
    private val webService: WebService
) : ReleaseNotesDataSource {

    override suspend fun getReleaseNotesResults(): ReleaseNotesResults =
        webService.releaseNotesResults()
}
