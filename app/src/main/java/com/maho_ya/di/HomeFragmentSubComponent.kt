package com.maho_ya.di

import com.maho_ya.ui.MainActivity
import com.maho_ya.ui.home.HomeFragment
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Subcomponent
interface HomeFragmentSubComponent : AndroidInjector<HomeFragment> {

    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<HomeFragment>
}

@Module(subcomponents = [HomeFragmentSubComponent::class])
interface HomeFragmentModule {

    @Binds
    @IntoMap
    @ClassKey(HomeFragment::class)
    fun bindHomeFragmentSubComponentFactory(
        factory: HomeFragmentSubComponent.Factory
    ): AndroidInjector.Factory<*>
}