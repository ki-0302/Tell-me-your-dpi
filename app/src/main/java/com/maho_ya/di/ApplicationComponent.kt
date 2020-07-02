package com.maho_ya.di

import android.content.Context
import com.maho_ya.ui.MainActivity
import com.maho_ya.ui.aboutapp.AboutAppFragment
import com.maho_ya.ui.home.HomeFragment
import com.maho_ya.ui.releasenotes.ReleaseNotesFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DeviceModule::class,
        ReleaseNotesModule::class,
        NetworkModule::class
    ]
)
interface ApplicationComponent {

    @Component.Builder
    interface Builder {
        fun build(): ApplicationComponent
        // @BindsInstance have been added since Dagger Ver.2.9. Arguments inject to component.
        @BindsInstance
        fun application(applicationContext: Context): Builder
    }

    fun inject(mainActivity: MainActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(releaseNotesFragment: ReleaseNotesFragment)
    fun inject(aboutAppFragment: AboutAppFragment)
}
