package com.maho_ya.tell_me_your_dpi

import androidx.multidex.MultiDexApplication
import timber.log.Timber

import timber.log.Timber.DebugTree




class MainApplication: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}