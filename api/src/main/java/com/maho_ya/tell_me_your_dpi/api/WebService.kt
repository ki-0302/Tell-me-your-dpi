package com.maho_ya.tell_me_your_dpi.api

import com.maho_ya.tell_me_your_dpi.model.ReleaseNotesResults
import retrofit2.http.GET

/**
 * REST API access points
 */
interface WebService {

    // Using suspend For Kotlin coroutine.
    @GET("api/release-notes")
    suspend fun releaseNotesResults(): ReleaseNotesResults
}
