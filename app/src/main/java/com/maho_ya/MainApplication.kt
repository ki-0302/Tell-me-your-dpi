package com.maho_ya

import androidx.multidex.MultiDexApplication
import com.maho_ya.di.ApplicationComponent
import com.maho_ya.di.DaggerApplicationComponent
import com.maho_ya.tell_me_your_dpi.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree

class MainApplication : MultiDexApplication() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerApplicationComponent
            .builder()
            .application(this.applicationContext)
            .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
