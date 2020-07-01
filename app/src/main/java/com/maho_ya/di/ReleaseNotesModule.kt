package com.maho_ya.di

import com.maho_ya.data.releasenotes.DataReleaseNotesDataSource
import com.maho_ya.data.releasenotes.DataReleaseNotesRepository
import com.maho_ya.data.releasenotes.ReleaseNotesDataSource
import com.maho_ya.data.releasenotes.ReleaseNotesRepository
import dagger.Binds
import dagger.Module

@Module
internal abstract class ReleaseNotesModule {

    @ActivityScope
    @Binds
    abstract fun provideReleaseNoteRepository(
        dataReleaseNotesRepository: DataReleaseNotesRepository
    ): ReleaseNotesRepository

    @Binds
    abstract fun provideReleaseNoteDataSource(
        dataReleaseNotesDataSource: DataReleaseNotesDataSource
    ): ReleaseNotesDataSource
}