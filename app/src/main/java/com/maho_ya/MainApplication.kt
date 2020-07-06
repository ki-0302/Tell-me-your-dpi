package com.maho_ya

import android.app.Activity
import androidx.multidex.MultiDexApplication
import com.maho_ya.di.ApplicationComponent
import com.maho_ya.di.DaggerApplicationComponent
import com.maho_ya.tell_me_your_dpi.BuildConfig
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import timber.log.Timber
import timber.log.Timber.DebugTree
import javax.inject.Inject

class MainApplication : MultiDexApplication(), HasAndroidInjector {

    // Required to DI activity, fragment
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        // Inject DispatchingAndroidInjector
        DaggerApplicationComponent
            .builder()
            .application(this)
            .build()
            .inject(this)

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    override fun androidInjector(): AndroidInjector<Any> {
        return dispatchingAndroidInjector
    }
}
