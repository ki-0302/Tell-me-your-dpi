package com.maho_ya.di

import com.maho_ya.ui.aboutapp.AboutAppFragment
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Subcomponent
interface AboutAppFragmentSubComponent : AndroidInjector<AboutAppFragment> {

    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<AboutAppFragment>
}

@Module(subcomponents = [AboutAppFragmentSubComponent::class])
interface AboutAppFragmentModule {

    @Binds
    @IntoMap
    @ClassKey(AboutAppFragment::class)
    fun bindAboutAppFragmentSubComponentFactory(
        factory: AboutAppFragmentSubComponent.Factory
    ): AndroidInjector.Factory<*>
}
