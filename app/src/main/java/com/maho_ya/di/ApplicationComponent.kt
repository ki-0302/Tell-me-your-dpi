package com.maho_ya.di

import com.maho_ya.MainApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Scope
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        MainActivityModule::class,
        HomeFragmentModule::class,
        ReleaseNotesFragmentModule::class,
        AboutAppFragmentModule::class,
        DeviceModule::class,
        ReleaseNotesModule::class,
        NetworkModule::class
    ]
)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(mainApplication: MainApplication): Builder
        fun build(): ApplicationComponent
    }

    fun inject(mainApplication: MainApplication)
}

@Scope
annotation class MainActivityScope