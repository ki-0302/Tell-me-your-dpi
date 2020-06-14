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

class ReleaseNotesDataSource {

    fun getReleaseNotes() {

        val webService = Retrofit.Builder()
            .baseUrl("https://maho-ya.firebaseapp.com/")
            .addConverterFactory(MoshiConverterFactory.create(createMoshiBuilder()))
            .build()
            .create(WebService::class.java)

        webService.releaseNotes().enqueue(object : Callback<ReleaseNotesResults> {
            override fun onFailure(call: Call<ReleaseNotesResults>, t: Throwable) {
                Timber.v("retrofit failed" + t.message)
            }

            override fun onResponse(call: Call<ReleaseNotesResults>, response: Response<ReleaseNotesResults>) {

                response.body()?.releaseNotes?.sortedBy { it.date }?.forEach {
                    Timber.v("retrofit ${it.date} ${it.appVersion} ${it.description}")
                }
            }
        })
    }

    fun createMoshiBuilder(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }


}