package com.maho_ya.tell_me_your_dpi.data.releasenotes

import com.maho_ya.tell_me_your_dpi.api.WebService
import javax.inject.Inject

interface ReleaseNotesDataSource {

    suspend fun getReleaseNotesResults(): com.maho_ya.tell_me_your_dpi.model.ReleaseNotesResults
}

class DataReleaseNotesDataSource @Inject constructor(
    private val webService: WebService
) : ReleaseNotesDataSource {

    override suspend fun getReleaseNotesResults(): com.maho_ya.tell_me_your_dpi.model.ReleaseNotesResults =
        webService.releaseNotesResults()
}
