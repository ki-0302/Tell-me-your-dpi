package com.maho_ya.di

import com.maho_ya.ui.MainActivity
import com.maho_ya.ui.aboutapp.AboutAppFragment
import com.maho_ya.ui.home.HomeFragment
import com.maho_ya.ui.releasenotes.ReleaseNotesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, DeviceModule::class, ReleaseNotesModule::class, NetworkModule::class]
)
interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(releaseNotesFragment: ReleaseNotesFragment)
    fun inject(aboutAppFragment: AboutAppFragment)
}
