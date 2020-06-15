package com.maho_ya.data.remote

import com.maho_ya.api.WebService
import com.maho_ya.model.ReleaseNote
import com.maho_ya.model.ReleaseNotesResults
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import com.maho_ya.result.Result
import kotlinx.coroutines.flow.Flow
import java.lang.IllegalStateException

class ReleaseNotesDataSource {

    suspend fun getReleaseNotes(): Result<List<ReleaseNote>> {

        val webService = Retrofit.Builder()
            .baseUrl("https://maho-ya.firebaseapp.com/")
            .addConverterFactory(MoshiConverterFactory.create(createMoshiBuilder()))
            .build()
            .create(WebService::class.java)

        return try {
            throw IllegalStateException("test")
            Result.Success(webService.releaseNotes().releaseNotes)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun createMoshiBuilder(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }


}