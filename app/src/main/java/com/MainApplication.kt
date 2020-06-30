package com

import androidx.multidex.MultiDexApplication
import com.maho_ya.di.DaggerApplicationComponent
import com.maho_ya.tell_me_your_dpi.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree

class MainApplication : MultiDexApplication() {

    val appComponent = DaggerApplicationComponent.create()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
