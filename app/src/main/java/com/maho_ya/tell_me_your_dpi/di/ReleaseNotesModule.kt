package com.maho_ya.tell_me_your_dpi.di

import com.maho_ya.tell_me_your_dpi.data.releasenotes.DataReleaseNotesDataSource
import com.maho_ya.tell_me_your_dpi.data.releasenotes.DataReleaseNotesRepository
import com.maho_ya.tell_me_your_dpi.data.releasenotes.ReleaseNotesDataSource
import com.maho_ya.tell_me_your_dpi.data.releasenotes.ReleaseNotesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class ReleaseNotesModule {

    @Singleton
    @Binds
    abstract fun provideReleaseNoteRepository(
        dataReleaseNotesRepository: DataReleaseNotesRepository
    ): ReleaseNotesRepository

    @Binds
    abstract fun provideReleaseNoteDataSource(
        dataReleaseNotesDataSource: DataReleaseNotesDataSource
    ): ReleaseNotesDataSource
}
