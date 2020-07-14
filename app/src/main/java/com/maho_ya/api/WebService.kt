package com.maho_ya.api

import com.maho_ya.model.ReleaseNotesResults
import retrofit2.http.GET

/**
 * REST API access points
 */
interface WebService {

    // Using suspend For Kotlin coroutine.
    @GET("api/release-notes")
    suspend fun releaseNotesResults(): com.maho_ya.model.ReleaseNotesResults
}
