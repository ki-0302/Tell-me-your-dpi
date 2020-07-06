package com.maho_ya.di

import com.maho_ya.ui.home.HomeFragment
import com.maho_ya.ui.releasenotes.ReleaseNotesFragment
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Subcomponent
interface ReleaseNotesFragmentSubComponent : AndroidInjector<ReleaseNotesFragment> {

    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<ReleaseNotesFragment>
}

@Module(subcomponents = [ReleaseNotesFragmentSubComponent::class])
interface ReleaseNotesFragmentModule {

    @Binds
    @IntoMap
    @ClassKey(ReleaseNotesFragment::class)
    fun bindReleaseNotesFragmentSubComponentFactory(
        factory: ReleaseNotesFragmentSubComponent.Factory
    ): AndroidInjector.Factory<*>
}