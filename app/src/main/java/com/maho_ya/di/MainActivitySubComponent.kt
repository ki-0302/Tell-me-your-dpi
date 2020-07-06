package com.maho_ya.di

import android.content.Context
import com.maho_ya.MainApplication
import com.maho_ya.ui.MainActivity
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@MainActivityScope
@Subcomponent
interface MainActivitySubComponent : AndroidInjector<MainActivity> {

    @Subcomponent.Factory
    interface Factory : AndroidInjector.Factory<MainActivity>
}

@Module(subcomponents = [MainActivitySubComponent::class])
interface MainActivityModule {

    @Binds
    @IntoMap
    @ClassKey(MainActivity::class)  // Added Key:MainActivity, Value:MainActivitySubComponent.Factory into MultiBindings.
    fun bindMainActivitySubComponentFactory(
        factory: MainActivitySubComponent.Factory
    ): AndroidInjector.Factory<*>

    @Singleton
    @Binds
    abstract fun provideApplicationContext(
        mainApplication: MainApplication
    ): Context
}
