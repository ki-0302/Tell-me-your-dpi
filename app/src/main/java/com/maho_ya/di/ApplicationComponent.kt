package com.maho_ya.di

import com.maho_ya.ui.MainActivity
import com.maho_ya.ui.releasenotes.ReleaseNotesFragment
import dagger.Component
import javax.inject.Scope

@ActivityScope
@Component(modules = [NetworkModule::class])
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(releaseNotesFragment: ReleaseNotesFragment)
}

