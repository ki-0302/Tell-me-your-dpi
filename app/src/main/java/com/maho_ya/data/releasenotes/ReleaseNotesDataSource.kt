package com.maho_ya.data.releasenotes

import com.maho_ya.api.WebService
import com.maho_ya.data.DataDownloader
import com.maho_ya.model.ReleaseNotesResults

interface ReleaseNotesDataSource {

    suspend fun getReleaseNotesResults(): ReleaseNotesResults
}

class DataReleaseNotesDataSource : ReleaseNotesDataSource {

    override suspend fun getReleaseNotesResults(): ReleaseNotesResults =
        DataDownloader(WebService::class.java).fetch().releaseNotesResults()
}