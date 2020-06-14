package com.maho_ya.api

import com.maho_ya.model.ReleaseNotesResults
import retrofit2.Call
import retrofit2.http.GET

/**
 * REST API access points
 */
interface WebService {

    @GET("api/release-notes")
    fun releaseNotes(): Call<ReleaseNotesResults>

}